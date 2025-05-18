package ru.practicum.collector.handler.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.BaseSensorEventHandler;
import ru.practicum.collector.handler.sensor.SensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.ClimateSensorEvent;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> implements SensorEventHandler {

    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent climateEvent = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateEvent.getTemperatureC())
                .setCo2Level(climateEvent.getCo2Level())
                .setHumidity(climateEvent.getHumidity())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}