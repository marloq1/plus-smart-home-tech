package ru.practicum.collector.handler.sensor.grpc.impl;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.grpc.BaseGrpcSensorEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class GrpcLightSensorEventHandler extends BaseGrpcSensorEventHandler implements GrpcSensorEventHandler {


    public GrpcLightSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected SpecificRecordBase mapToAvro(SensorEventProto event) {
        LightSensorProto lightEvent = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightEvent.getLinkQuality())
                .setLuminosity(lightEvent.getLuminosity())
                .build();
    }
}
