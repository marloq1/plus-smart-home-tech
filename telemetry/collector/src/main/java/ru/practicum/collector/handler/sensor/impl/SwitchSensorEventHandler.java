package ru.practicum.collector.handler.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.BaseSensorEventHandler;
import ru.practicum.collector.handler.sensor.SensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;
import ru.practicum.collector.model.sensors.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> implements SensorEventHandler {

    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent switchEvent = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.isState())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
