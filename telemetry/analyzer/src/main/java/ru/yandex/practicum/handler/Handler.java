package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.reposipory.ActionRepository;
import ru.yandex.practicum.reposipory.ConditionRepository;
import ru.yandex.practicum.reposipory.ScenarioRepository;
import ru.yandex.practicum.reposipory.SensorRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
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
        } catch (DataIntegrityViolationException e   ) {
            log.info("Ошибка записи в бд");
        } catch (NotFoundException e) {
            log.info("Ошибка удаления из бд");
        }
        
    }

    public void handle(SensorsSnapshotAvro sensorsSnapshotAvro) {

        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshotAvro.getHubId());
        System.out.println();
    }
}
