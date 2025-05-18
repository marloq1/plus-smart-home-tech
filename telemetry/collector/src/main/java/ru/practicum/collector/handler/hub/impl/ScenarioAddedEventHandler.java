package ru.practicum.collector.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.hub.BaseHubEventHandler;
import ru.practicum.collector.handler.hub.HubMapper;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.hubs.HubEvent;
import ru.practicum.collector.model.hubs.HubEventType;
import ru.practicum.collector.model.hubs.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setAction(scenarioAddedEvent.getActions().stream().map(HubMapper::mapActionToAvro).toList())
                .setConditions(scenarioAddedEvent.getConditions()
                        .stream().map(HubMapper::mapScenarioConditionToAvro).toList())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }



}
