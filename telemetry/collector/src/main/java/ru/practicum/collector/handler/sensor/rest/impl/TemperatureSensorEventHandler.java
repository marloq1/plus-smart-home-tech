package ru.practicum.collector.handler.sensor.rest.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.rest.BaseSensorEventHandler;
import ru.practicum.collector.handler.sensor.rest.SensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;
import ru.practicum.collector.model.sensors.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> implements SensorEventHandler {

    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent temperatureEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureEvent.getTemperatureC())
                .setTemperatureF(temperatureEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}