package ru.yandex.practicum.handler;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.reposipory.ScenarioRepository;
import ru.yandex.practicum.reposipory.SensorRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    public void handle(HubEventAvro hubEventAvro) {
        try {
            switch (hubEventAvro.getPayload()) {
                case DeviceAddedEventAvro eventAvro -> sensorRepository.save(Sensor.builder()
                        .id(eventAvro.getId())
                        .hubId(hubEventAvro.getHubId())
                        .build());
                case DeviceRemovedEventAvro eventAvro -> sensorRepository
                        .delete(sensorRepository.findByIdAndHubId(eventAvro.getId(), hubEventAvro.getHubId())
                                .orElseThrow(() -> new NotFoundException("Такого датчика нет")));
                case ScenarioAddedEventAvro eventAvro -> scenarioRepository.save(Scenario.builder()
                        .name(eventAvro.getName())
                        .hubId(hubEventAvro.getHubId())
                        .actions(IntStream.range(0, eventAvro.getAction().size())
                                .boxed()
                                .collect(Collectors.toMap(
                                        eventAvro.getAction().stream().map(DeviceActionAvro::getSensorId)
                                                .toList()::get,
                                        eventAvro.getAction().stream().map(action -> Action.builder()
                                                .actionType(ActionType.valueOf(action.getType().name()))
                                                .value(action.getValue()).build()).toList()::get
                                )))
                        .conditions(IntStream.range(0, eventAvro.getConditions().size())
                                .boxed()
                                .collect(Collectors.toMap(eventAvro.getConditions().stream()
                                                .map(ScenarioConditionAvro::getSensorId).toList()::get,
                                        eventAvro.getConditions().stream().map(condition -> Condition.builder()
                                                .type(ConditionType.valueOf(condition.getType().name()))
                                                .operation(ConditionOperation.valueOf(condition.getOperation().name()))
                                                .value(condition.getValue() instanceof Integer i ? i :
                                                        condition.getValue() instanceof Boolean b ? (b ? 1 : 0) :
                                                                0).build()).toList()::get)))
                        .build());
                case ScenarioRemovedEventAvro eventAvro -> scenarioRepository.delete(scenarioRepository
                        .findByHubIdAndName(hubEventAvro.getHubId(), eventAvro.getName())
                        .orElseThrow(() -> new NotFoundException("Такого сценария нет")));
                default -> throw new RuntimeException();
            }
        } catch (DataIntegrityViolationException e) {
            log.info("Ошибка записи в бд");
        } catch (NotFoundException e) {
            log.info("Ошибка удаления из бд");
        }

    }

    @Transactional
    public List<DeviceActionRequest> handle(SensorsSnapshotAvro sensorsSnapshotAvro) {


        List<DeviceActionRequest> requests = new ArrayList<>();
        boolean isMatched;
        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshotAvro.getHubId());
        log.info("Выгружена информация из бд по сценариям");
        for (Scenario scenario : scenarios) {
            isMatched = true;
            Map<String, Condition> conditions = scenario.getConditions();
            Map<String, Action> actions = scenario.getActions();
            log.info("Проверка совпадений для сценария {}", scenario.getName());
            for (Condition condition : conditions.values()) {
                log.info("{}, {}, {}", condition.getType(), condition.getOperation(), condition.getValue());
            }
            if (!sensorsSnapshotAvro.getSensorsState().keySet().containsAll(conditions.keySet())) {
                log.info("Не совпадают типы датчиков");
                log.info("{}", sensorsSnapshotAvro.getSensorsState().keySet());
                conditions.keySet().stream().peek(s -> log.info("{},", s));
                continue;
            }
            for (String key : conditions.keySet()) {
                Condition condition = conditions.get(key);
                Object data = sensorsSnapshotAvro.getSensorsState().get(key).getData();
                try {
                    switch (condition.getType()) {
                        case MOTION -> {
                            MotionSensorAvro motionData = (MotionSensorAvro) data;
                            log.info("MOTION {}", motionData.getMotion());
                            isMatched = (((MotionSensorAvro) data).getMotion() == (condition.getValue() != 0));
                        }
                        case SWITCH -> {
                            SwitchSensorAvro switchData = (SwitchSensorAvro) data;
                            log.info("SWITCH {}", switchData.getState());
                            isMatched = (((SwitchSensorAvro) data).getState() == (condition.getValue() != 0));
                        }
                        case LUMINOSITY -> {
                            LightSensorAvro lightData = (LightSensorAvro) data;
                            log.info("LUMINOSITY {}", lightData.getLuminosity());
                            isMatched = Boolean.FALSE.equals(compare(((LightSensorAvro) data)
                                    .getLuminosity(), (condition.getValue()), condition.getOperation()));
                        }
                        case TEMPERATURE -> {
                            if (data instanceof TemperatureSensorAvro temperatureData) {
                                log.info("TEMPERATURE {}", temperatureData.getTemperatureC());
                                isMatched = Boolean.FALSE.equals(compare(((TemperatureSensorAvro) data)
                                        .getTemperatureC(), condition.getValue(), condition.getOperation()));
                            } else if (data instanceof ClimateSensorAvro climateData) {
                                log.info("TEMPERATURE(C) {}", climateData.getTemperatureC());
                                isMatched = Boolean.FALSE.equals(compare(((ClimateSensorAvro) data)
                                        .getTemperatureC(), condition.getValue(), condition.getOperation()));
                            } else {
                                throw new ClassCastException();
                            }
                        }
                        case CO2LEVEL -> {
                            ClimateSensorAvro climateData = (ClimateSensorAvro) data;
                            log.info("CO2LEVEL {}", climateData.getCo2Level());
                            isMatched = Boolean.FALSE.equals(compare(((ClimateSensorAvro) data)
                                    .getCo2Level(), condition.getValue(), condition.getOperation()));
                        }
                        case HUMIDITY -> {
                            ClimateSensorAvro climateData = (ClimateSensorAvro) data;
                            log.info("Humidity {}", climateData.getHumidity());
                            isMatched = Boolean.FALSE.equals(compare(((ClimateSensorAvro) data)
                                    .getHumidity(), condition.getValue(), condition.getOperation()));
                        }
                    }
                } catch (ClassCastException e) {
                    log.info("Тип классов не совпал");
                    isMatched = false;
                }
            }

            if (isMatched) {
                log.info("Найдены совпадения");
                Instant instant = Instant.now();

                requests.addAll(actions.entrySet().stream().map(entry -> DeviceActionRequest
                        .newBuilder()
                        .setHubId(scenario.getHubId())
                        .setScenarioName(scenario.getName())
                        .setAction(DeviceActionProto.newBuilder()
                                .setSensorId(entry.getKey())
                                .setType(ActionTypeProto.valueOf(entry.getValue().getActionType().name()))
                                .setValue(entry.getValue().getValue())
                                .build())
                        .setTimestamp(Timestamp.newBuilder()
                                .setSeconds(instant.getEpochSecond())
                                .setNanos(instant.getNano())
                                .build())
                        .build()).toList());
            } else {
                log.info("Условия сценария не выполнены");
            }
        }

        return requests;

    }

    private Boolean compare(Integer val1, Integer val2, ConditionOperation operation) {
        switch (operation) {
            case EQUALS -> {
                return Objects.equals(val1, val2);
            }
            case LOWER_THAN -> {
                return val1 > val2;
            }
            case GREATER_THAN -> {
                return val1 < val2;
            }
            default -> {
                return null;
            }
        }

    }
}
