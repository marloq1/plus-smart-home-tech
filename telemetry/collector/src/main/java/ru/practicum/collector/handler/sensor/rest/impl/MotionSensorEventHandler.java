package ru.practicum.collector.handler.sensor.rest.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.rest.BaseSensorEventHandler;
import ru.practicum.collector.handler.sensor.rest.SensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.MotionSensorEvent;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> implements SensorEventHandler {

    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent motionEvent = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionEvent.getLinkQuality())
                .setMotion(motionEvent.isMotion())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}