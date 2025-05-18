package ru.practicum.collector.handler.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.BaseSensorEventHandler;
import ru.practicum.collector.handler.sensor.SensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.LightSensorEvent;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> implements SensorEventHandler {

    public LightSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
       LightSensorEvent lightEvent = (LightSensorEvent) event;
       return LightSensorAvro.newBuilder()
               .setLinkQuality(lightEvent.getLinkQuality())
               .setLuminosity(lightEvent.getLuminosity())
               .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
