package ru.practicum.collector.handler.sensor.grpc.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.grpc.BaseGrpcSensorEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Slf4j
@Component
public class GrpcTemperatureSensorEventHandler extends BaseGrpcSensorEventHandler<TemperatureSensorAvro> implements GrpcSensorEventHandler {

    public GrpcTemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto temperatureEvent = event.getTemperatureSensorEvent();
        log.info("TempC {}, TempF {}", temperatureEvent.getTemperatureC(), temperatureEvent.getTemperatureF());
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureEvent.getTemperatureC())
                .setTemperatureF(temperatureEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {

        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
