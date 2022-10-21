package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RecordSerializer extends StdSerializer<Record> {

    public RecordSerializer() {
        this(null);
    }

    public RecordSerializer(Class<Record> t) {
        super(t);
    }

    @Override
    public void serialize(Record value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeObjectField("updateTime", value.getUpdateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        for (Map.Entry<String, Object> entry : value.getRequisiteValueMap().entrySet())
            jgen.writeObjectField(entry.getKey(), entry.getValue());

        jgen.writeEndObject();
    }

}
