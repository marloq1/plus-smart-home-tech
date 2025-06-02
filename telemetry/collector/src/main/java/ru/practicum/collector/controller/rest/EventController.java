package ru.practicum.collector.controller.rest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.handler.hub.rest.HubEventHandler;
import ru.practicum.collector.handler.sensor.rest.SensorEventHandler;
import ru.practicum.collector.model.hubs.HubEvent;
import ru.practicum.collector.model.hubs.HubEventType;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.practicum.collector.model.sensors.SensorEventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
public class EventController {

    private final Map<SensorEventType, SensorEventHandler> sensorEventsMap;
    private final Map<HubEventType, HubEventHandler> hubEventsMap;

    public EventController(List<SensorEventHandler> sensorEvents,List<HubEventHandler> hubEvents) {
        this.sensorEventsMap = sensorEvents.stream().collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventsMap = hubEvents.stream().collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {

        if (sensorEventsMap.containsKey(event.getType())) {
            sensorEventsMap.get(event.getType()).handle(event, "telemetry.sensors.v1");
        } else {
            throw new IllegalArgumentException("Нет такого события");
        }
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        if (hubEventsMap.containsKey(event.getType())) {
            hubEventsMap.get(event.getType()).handle(event, "telemetry.hubs.v1");
        } else {
            throw new IllegalArgumentException("Нет такого события");
        }

    }


}
