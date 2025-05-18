package ru.practicum.collector.model.hubs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ScenarioAddedEvent extends HubEvent {

    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;
    private HubEventType type = HubEventType.SCENARIO_ADDED;

}
