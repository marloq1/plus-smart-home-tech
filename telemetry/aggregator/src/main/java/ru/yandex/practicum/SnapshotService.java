package ru.yandex.practicum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.*;

@Service
public class SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotService.class);
    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.info("Хаб 1 - {}", snapshots.get("hub-1"));
        log.info("Хаб 2 - {}", snapshots.get("hub-2"));
        if (!snapshots.containsKey(event.getHubId())) {
            SensorsSnapshotAvro snap = createSnapShot(event);
            snapshots.put(event.getHubId(), snap);
            log.info("Первая запись в хаб {}", event.getHubId());
            return Optional.of(snap);
        }
        if (!snapshots.get(event.getHubId()).getSensorsState().containsKey(event.getId())) {
            SensorsSnapshotAvro snap = snapshots.get(event.getHubId());

            Map<String, SensorStateAvro> mutableMap = new HashMap<>(snap.getSensorsState());
            mutableMap.put(event.getId(), SensorStateAvro.newBuilder().setTimestamp(event.getTimestamp())
                    .setData(event.getPayload()).build());
            snap.setSensorsState(mutableMap);
            snapshots.put(event.getHubId(), snap);
            log.info("Первый датчик с id {}, типа {} в хаб {}", event.getId(),
                    event.getPayload().getClass(), event.getHubId());

            return Optional.of(snap);
        }
        SensorStateAvro oldState = snapshots.get(event.getHubId()).getSensorsState().get(event.getId());
        if (oldState.getTimestamp().isAfter(event.getTimestamp())) {
            log.info("Событие произошло раньше");
            return Optional.empty();
        }
        if ((oldState.getData() instanceof ClimateSensorAvro oldClimate)
                && (event.getPayload() instanceof ClimateSensorAvro newClimate)) {
            log.info("Старое состояние: Время {}, СO2 {}, Hum {}, Temp {}", oldState.getTimestamp(),
                    oldClimate.getCo2Level(), oldClimate.getHumidity(), oldClimate.getTemperatureC());
            log.info("Новое состояние: Время {}, СO2 {}, Hum {}, Temp {}", event.getTimestamp(),
                    newClimate.getCo2Level(), newClimate.getHumidity(), newClimate.getTemperatureC());
            if (oldClimate.getCo2Level() == newClimate.getCo2Level() &&
                    oldClimate.getHumidity() == newClimate.getHumidity()
                    && oldClimate.getTemperatureC() == newClimate.getTemperatureC()) {
                return Optional.empty();
            }
        }
        if ((oldState.getData() instanceof TemperatureSensorAvro oldTemperature)
                && (event.getPayload() instanceof TemperatureSensorAvro newTemperature)) {
            log.info("Старое состояние: Время {}, TempC {}, TempF {}", oldState.getTimestamp(),
                    oldTemperature.getTemperatureC(), oldTemperature.getTemperatureF());
            log.info("Новое состояние: Время {}, TempC {}, TempF {}", event.getTimestamp(),
                    newTemperature.getTemperatureC(), newTemperature.getTemperatureF());
            if (oldTemperature.getTemperatureC() == newTemperature.getTemperatureC() &&
                    oldTemperature.getTemperatureF() == newTemperature.getTemperatureF()) {
                return Optional.empty();
            }
        }

        if ((oldState.getData() instanceof LightSensorAvro oldLight)
                && (event.getPayload() instanceof LightSensorAvro newLight)) {
            log.info("Старое состояние: Время {}, Lum {}, LinkQ {}", oldState.getTimestamp(),
                    oldLight.getLuminosity(), oldLight.getLinkQuality());
            log.info("Новое состояние: Время {}, Lum {}, LinkQ {}", event.getTimestamp(), newLight.getLuminosity(),
                    newLight.getLinkQuality());
            if (oldLight.getLuminosity() == newLight.getLuminosity() &&
                    oldLight.getLinkQuality() == newLight.getLinkQuality()) {
                return Optional.empty();
            }
        }

        if ((oldState.getData() instanceof MotionSensorAvro oldMotion)
                && (event.getPayload() instanceof MotionSensorAvro newMotion)) {
            log.info("Старое состояние: Время {}, Motion {}, LinkQ {}, Voltage {}", oldState.getTimestamp(),
                    oldMotion.getMotion(), oldMotion.getLinkQuality(), oldMotion.getVoltage());
            log.info("Новое состояние: Время {}, Motion {}, LinkQ {}, Voltage {}", event.getTimestamp(),
                    newMotion.getMotion(), newMotion.getLinkQuality(), newMotion.getVoltage());
            if (oldMotion.getMotion() == newMotion.getMotion() &&
                    oldMotion.getLinkQuality() == newMotion.getLinkQuality() &&
                    oldMotion.getVoltage() == newMotion.getVoltage()) {
                return Optional.empty();
            }
        }


        if ((oldState.getData() instanceof SwitchSensorAvro oldSwitch)
                && (event.getPayload() instanceof SwitchSensorAvro newSwitch)) {
            log.info("Старое состояние: Время {}, State {}", oldState.getTimestamp(),
                    oldSwitch.getState());
            log.info("Новое состояние: Время {}, State {}", event.getTimestamp(),
                    newSwitch.getState());
            if (oldSwitch.getState() == newSwitch.getState()) {
                return Optional.empty();
            }
        }

        SensorsSnapshotAvro snap = snapshots.get(event.getHubId());

        Map<String, SensorStateAvro> mutableMap = new HashMap<>(snap.getSensorsState());
        mutableMap.put(event.getId(), SensorStateAvro.newBuilder().setTimestamp(event.getTimestamp())
                .setData(event.getPayload()).build());
        snap.setSensorsState(mutableMap);
        snapshots.put(event.getHubId(), snap);
        log.info("Данные обновлены");
        return Optional.of(snap);


    }


    private SensorsSnapshotAvro createSnapShot(SensorEventAvro eventAvro) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(eventAvro.getHubId())
                .setTimestamp(eventAvro.getTimestamp())
                .setSensorsState(Map.of(eventAvro.getId(), SensorStateAvro.newBuilder()
                        .setTimestamp(eventAvro.getTimestamp())
                        .setData(eventAvro.getPayload())
                        .build()))
                .build();
    }

}
