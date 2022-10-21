package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.service.RequisiteService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecordControllerDeserializer {

    private final RequisiteService requisiteService;

    public RecordControllerDeserializer(RequisiteService requisiteService) {
        this.requisiteService = requisiteService;
    }

    public Record deserialize(String customTableId, Map<String, String> params) {
        List<Requisite> requisites = requisiteService.findByCustomTableId(customTableId);
        Map<String, Object> requisiteValueMap = new HashMap<>();

        Record record = new Record();
        record.setCustomTableId(customTableId);

        if (!params.get("id").isEmpty())
            record.setId(Long.parseLong(params.get("id")));

        requisites.forEach(x -> addToMap(x, params.get(x.getId()), requisiteValueMap));

        record.setRequisiteValueMap(requisiteValueMap);

        return record;
    }

    private void addToMap(Requisite requisite, String s, Map<String, Object> requisiteValueMap) {
        if (requisite.getType() == RequisiteType.INTEGER) {
            int result = 0;

            try {
                result = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }

            requisiteValueMap.put(requisite.getId(), result);
        } else if (requisite.getType() == RequisiteType.LONG) {
            long result = 0;

            try {
                result = Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }

            requisiteValueMap.put(requisite.getId(), result);
        } else if (requisite.getType() == RequisiteType.DOUBLE) {
            double result = 0;

            try {
                result = Double.parseDouble(s);
            } catch (NumberFormatException ignored) {
            }

            requisiteValueMap.put(requisite.getId(), result);
        } else if (requisite.getType() == RequisiteType.FOREIGN) {
            Long result = null;

            try {
                result = Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }

            requisiteValueMap.put(requisite.getId(), result);
        } else if (requisite.getType() == RequisiteType.BOOLEAN) {
            requisiteValueMap.put(requisite.getId(), Boolean.parseBoolean(s));
        } else {
            requisiteValueMap.put(requisite.getId(), s);
        }

    }

    public Record checkRequisites(String customTableId, Record record) {
        List<Requisite> requisites = requisiteService.findByCustomTableId(customTableId);
        Set<String> requisitesId = requisites.stream().map(Requisite::getId).collect(Collectors.toSet());
        record.setCustomTableId(customTableId);

        record.getRequisiteValueMap().entrySet().removeIf(x -> !requisitesId.contains(x.getKey()));

        return record;
    }
}
