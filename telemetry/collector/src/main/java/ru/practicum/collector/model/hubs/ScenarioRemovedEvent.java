package ru.practicum.collector.model.hubs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenarioRemovedEvent extends HubEvent {

    private String name;
    private HubEventType type = HubEventType.SCENARIO_REMOVED;
}
