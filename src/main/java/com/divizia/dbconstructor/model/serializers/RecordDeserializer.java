package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecordDeserializer extends StdDeserializer<Record> {

    public RecordDeserializer() {
        this(null);
    }

    public RecordDeserializer(Class<Record> t) {
        super(t);
    }

    @Override
    public Record deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        Record record = new Record();
        Map<String, Object> requisiteValueMap = new HashMap<>();

        JsonNode mainNode = jp.getCodec().readTree(jp);

        mainNode.fieldNames().forEachRemaining(x -> {
            if (x.equals("id"))
                record.setId((Long) mainNode.get("id").numberValue());
            else {
                addToMap(requisiteValueMap, mainNode, x);
            }
        });

        record.setRequisiteValueMap(requisiteValueMap);

        return record;
    }

    private void addToMap(Map<String, Object> requisiteValueMap, JsonNode mainNode, String x) {
        JsonNode node = mainNode.get(x);
        if (node.isNull()) return;

        if (node.isInt()) {
            requisiteValueMap.put(x, node.asInt());
        } else if (node.isLong()) {
            requisiteValueMap.put(x, node.asLong());
        } else if (node.isDouble()) {
            requisiteValueMap.put(x, node.asDouble());
        } else if (node.isBoolean()) {
            requisiteValueMap.put(x, node.asBoolean());
        } else {
            requisiteValueMap.put(x, node.asText());
        }
    }

}
