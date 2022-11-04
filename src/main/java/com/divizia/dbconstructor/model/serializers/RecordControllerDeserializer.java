package com.divizia.dbconstructor.model.serializers;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.service.RequisiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RecordControllerDeserializer {

    private final RequisiteService requisiteService;

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
        } else if (requisite.getType() == RequisiteType.LOCAL_DATE_TIME) {
            LocalDateTime result = null;

            try {
                result = LocalDateTime.parse(s);
            } catch (DateTimeParseException ignored) {
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
        record.setCustomTableId(customTableId);

        List<Requisite> requisites = requisiteService.findByCustomTableId(customTableId);
        Map<String, Requisite> requisitesWithId = requisites.stream().collect(Collectors.toMap(Requisite::getId, x -> x));

        Set<Map.Entry<String, Object>> entrySet = record.getRequisiteValueMap().entrySet();
        entrySet.removeIf(x -> !requisitesWithId.containsKey(x.getKey()));
        entrySet.forEach(x -> convertRequisite(requisitesWithId, x));

        return record;
    }

    private void convertRequisite(Map<String, Requisite> requisitesWithId, Map.Entry<String, Object> x) {
        RequisiteType requisiteType = requisitesWithId.get(x.getKey()).getType();
        if (requisiteType == RequisiteType.LOCAL_DATE_TIME && x.getValue() instanceof String) {
            LocalDateTime result = null;

            try {
                result = LocalDateTime.parse(x.getValue().toString(), Formatter.formatNormal);
            } catch (DateTimeParseException ignored) {
            }

            x.setValue(result);
        }
    }
}
