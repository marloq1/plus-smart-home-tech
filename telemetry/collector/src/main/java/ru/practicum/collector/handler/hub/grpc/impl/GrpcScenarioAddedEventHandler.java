package ru.practicum.collector.handler.hub.grpc.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.hub.grpc.GrpcBaseHubEventHandler;
import ru.practicum.collector.handler.HubMapper;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Component
public class GrpcScenarioAddedEventHandler extends GrpcBaseHubEventHandler<ScenarioAddedEventAvro> {

    public GrpcScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setAction(scenarioAddedEvent.getActionList().stream().map(HubMapper::mapActionProtoToAvro).toList())
                .setConditions(scenarioAddedEvent.getConditionList()
                        .stream().map(HubMapper::mapScenarioConditionProtoToAvro).toList())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }



}
