package ru.practicum.collector.handler.sensor.grpc.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.sensor.grpc.BaseGrpcSensorEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Slf4j
@Component
public class GrpcMotionSensorEventHandler extends BaseGrpcSensorEventHandler implements GrpcSensorEventHandler {


    public GrpcMotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    protected SpecificRecordBase mapToAvro(SensorEventProto event) {
        MotionSensorProto motionEvent = event.getMotionSensorEvent();
        log.info("Motion {},LinKQ {},Voltage {}", motionEvent.getMotion(), motionEvent.getLinkQuality(),
                motionEvent.getVoltage());
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionEvent.getLinkQuality())
                .setMotion(motionEvent.getMotion())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }
}