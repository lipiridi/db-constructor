package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class RecordJsonSerializer extends StdSerializer<Record> {

    public RecordJsonSerializer() {
        this(null);
    }

    public RecordJsonSerializer(Class<Record> t) {
        super(t);
    }

    @Override
    public void serialize(Record value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeObjectField("updateTime", value.getUpdateTime().format(Formatter.formatNormal));

        for (Map.Entry<String, Object> entry : value.getRequisiteValueMap().entrySet()) {
            Object valueObject = entry.getValue();
            if (valueObject instanceof LocalDateTime)
                jgen.writeObjectField(entry.getKey(), ((LocalDateTime) valueObject).format(Formatter.formatNormal));
            else
                jgen.writeObjectField(entry.getKey(), valueObject);
        }

        jgen.writeEndObject();
    }

}
