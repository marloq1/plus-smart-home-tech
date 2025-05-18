package ru.practicum.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClimateSensorEvent extends SensorEvent {

    private int temperatureC;
    private int humidity;
    private int co2Level;
    private SensorEventType type = SensorEventType.CLIMATE_SENSOR_EVENT;
}
