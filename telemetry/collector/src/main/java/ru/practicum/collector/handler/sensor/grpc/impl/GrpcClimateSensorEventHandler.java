package ru.practicum.collector.handler.sensor.grpc.impl;

import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.grpc.BaseGrpcSensorEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class GrpcClimateSensorEventHandler extends BaseGrpcSensorEventHandler implements GrpcSensorEventHandler {


    private static final Logger log = LoggerFactory.getLogger(GrpcClimateSensorEventHandler.class);

    public GrpcClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected SpecificRecordBase mapToAvro(SensorEventProto event) {
        ClimateSensorProto climateEvent = event.getClimateSensorEvent();
        log.info("TempC {}, CO2 {}, Hum {}",climateEvent.getTemperatureC(),climateEvent.getCo2Level(),
                climateEvent.getHumidity());
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateEvent.getTemperatureC())
                .setCo2Level(climateEvent.getCo2Level())
                .setHumidity(climateEvent.getHumidity())
                .build();
    }
}
