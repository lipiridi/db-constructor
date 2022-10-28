package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.service.impl.IdChecker;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecordJsonDeserializer extends StdDeserializer<Record> {

    public RecordJsonDeserializer() {
        this(null);
    }

    public RecordJsonDeserializer(Class<Record> t) {
        super(t);
    }

    @Override
    public Record deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        Record record = new Record();
        Map<String, Object> requisiteValueMap = new HashMap<>();

        JsonNode mainNode = jp.getCodec().readTree(jp);

        mainNode.fields().forEachRemaining(x -> {
            String key = IdChecker.checkId(x.getKey());

            if (key.equals("a_id"))
                record.setId(x.getValue().asLong());
            else {
                addToMap(requisiteValueMap, x.getValue(), key);
            }
        });

        record.setRequisiteValueMap(requisiteValueMap);

        return record;
    }

    private void addToMap(Map<String, Object> requisiteValueMap, JsonNode node, String x) {
        //JsonNode node = mainNode.get(x);

        if (node.isInt()) {
            requisiteValueMap.put(x, node.asInt());
        } else if (node.isLong()) {
            requisiteValueMap.put(x, node.asLong());
        } else if (node.isDouble()) {
            requisiteValueMap.put(x, node.asDouble());
        } else if (node.isBoolean()) {
            requisiteValueMap.put(x, node.asBoolean());
        } else if (node.isNull()) {
            requisiteValueMap.put(x, null);
        } else {
            requisiteValueMap.put(x, node.asText());
        }
    }

}
