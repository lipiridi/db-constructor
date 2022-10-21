package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.divizia.dbconstructor.model.serializers.RecordDeserializer;
import com.divizia.dbconstructor.model.serializers.RecordSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@JsonSerialize(using = RecordSerializer.class)
@JsonDeserialize(using = RecordDeserializer.class)
public class Record implements Updatable<Record> {

    private Long id;
    private String customTableId;
    private LocalDateTime updateTime;
    private Map<String, Object> requisiteValueMap;

    @Override
    public Record updateAllowed(Record other) {
        if (!id.equals(other.id) || !customTableId.equals(other.customTableId))
            return this;

        other.requisiteValueMap.forEach((x, y) -> {
            if (y != null)
                requisiteValueMap.put(x, y);
        });

        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("id=" + id);
        requisiteValueMap.forEach((x, y) -> stringBuilder.append(",\n").append(x).append("=").append(y));
        return stringBuilder.toString();
    }
}
