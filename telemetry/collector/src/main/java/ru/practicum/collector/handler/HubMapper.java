package ru.practicum.collector.handler;

import lombok.experimental.UtilityClass;
import ru.practicum.collector.model.hubs.DeviceAction;
import ru.practicum.collector.model.hubs.ScenarioCondition;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

@UtilityClass
public class HubMapper {

    public static DeviceActionAvro mapActionToAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();
    }

    public static ScenarioConditionAvro mapScenarioConditionToAvro(ScenarioCondition action) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setOperation(ConditionOperationAvro.valueOf(action.getOperation().name()))
                .setType(ConditionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();
    }

    public static DeviceActionAvro mapActionProtoToAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();
    }

    public static ScenarioConditionAvro mapScenarioConditionProtoToAvro(ScenarioConditionProto action) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setOperation(ConditionOperationAvro.valueOf(action.getOperation().name()))
                .setType(ConditionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValueCase().equals(ScenarioConditionProto.ValueCase.BOOL_VALUE)?
                        action.getBoolValue() : action.getIntValue())
                .build();
    }


}
