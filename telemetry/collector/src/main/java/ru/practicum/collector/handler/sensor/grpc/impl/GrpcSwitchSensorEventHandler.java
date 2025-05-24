package ru.practicum.collector.handler.sensor.grpc.impl;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.grpc.BaseGrpcSensorEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class GrpcSwitchSensorEventHandler extends BaseGrpcSensorEventHandler implements GrpcSensorEventHandler {


    public GrpcSwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SpecificRecordBase mapToAvro(SensorEventProto event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getSwitchSensorEvent().getState())
                .build();
    }
}
