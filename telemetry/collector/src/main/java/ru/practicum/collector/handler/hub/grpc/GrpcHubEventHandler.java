package ru.practicum.collector.handler.hub.grpc;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface GrpcHubEventHandler {

    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}
