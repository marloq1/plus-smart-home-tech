package ru.practicum.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TemperatureSensorEvent extends SensorEvent {

    private int temperatureC;
    private int temperatureF;
    private SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;
}
