package ru.practicum.collector.controller.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.collector.handler.hub.grpc.GrpcHubEventHandler;
import ru.practicum.collector.handler.sensor.grpc.GrpcSensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class GrpcEventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final Map<SensorEventProto.PayloadCase, GrpcSensorEventHandler> sensorEventsMap;
    private final Map<HubEventProto.PayloadCase, GrpcHubEventHandler> hubEventsMap;


    public GrpcEventController(List<GrpcSensorEventHandler> sensorEvents, List<GrpcHubEventHandler> hubEvents) {
        this.sensorEventsMap = sensorEvents.stream().collect(Collectors.toMap(GrpcSensorEventHandler::getMessageType, Function.identity()));
        this.hubEventsMap = hubEvents.stream().collect(Collectors.toMap(GrpcHubEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (sensorEventsMap.containsKey(request.getPayloadCase())) {
                sensorEventsMap.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Нет такого события");
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (hubEventsMap.containsKey(request.getPayloadCase())) {
                hubEventsMap.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Нет такого события");
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}