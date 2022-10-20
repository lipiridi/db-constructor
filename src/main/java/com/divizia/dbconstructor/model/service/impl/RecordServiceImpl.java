package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.repo.CustomTableRepository;
import com.divizia.dbconstructor.model.service.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final CustomTableRepository customTableRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Record saveAndFlush(Record record) {

        Long id = record.getId();
        String idString = id == null || id == 0 ? "default" : id.toString();

        StringBuilder columns = new StringBuilder("id");
        StringBuilder values = new StringBuilder(idString);
        StringBuilder conflict = new StringBuilder("update_time=EXCLUDED.update_time");
        record.getRequisiteValueMap().forEach((x, y) -> {
            columns.append(",").append(x);
            values.append(",").append(RequisiteType.toStringForDB(y));
            conflict.append(",").append(x).append("=EXCLUDED.").append(x);
        });

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(String.format(
                        "INSERT INTO c_books(%s) VALUES (%s) ON CONFLICT (id) DO UPDATE SET %s RETURNING id, update_time",
                        columns,
                        values,
                        conflict));
        Map<String, Object> result = mapList.get(0);

        record.setId((Long) result.get("id"));
        record.setUpdateTime(((Timestamp) result.get("update_time")).toLocalDateTime());

        return record;
    }

    @Override
    public void deleteById(String customTableId, Long recordId) {
        jdbcTemplate.execute(String.format("delete from %s where id = %s", customTableId, recordId));
    }

    @Override
    public Optional<Record> findById(String customTableId, Long recordId) {

        Optional<Record> optionalRecord = Optional.empty();

        Optional<CustomTable> customTable = customTableRepository.findById(customTableId);
        if (customTable.isEmpty())
            return optionalRecord;

        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(
                String.format("select * from %s where id = %s", customTableId, recordId));
        if (queryForList.isEmpty())
            return optionalRecord;

        Map<String, Object> params = queryForList.get(0);

        optionalRecord = Optional.of(createRecordFromEntry(customTable.get().getId(), params));

        return optionalRecord;
    }

    @Override
    public List<Record> findAll(String customTableId) {

        List<Record> recordList = new ArrayList<>();

        Optional<CustomTable> customTable = customTableRepository.findById(customTableId);
        if (customTable.isEmpty())
            return recordList;

        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(
                String.format("select * from %s", customTableId));
        if (queryForList.isEmpty())
            return recordList;

        for (Map<String, Object> params : queryForList)
            recordList.add(createRecordFromEntry(customTable.get().getId(), params));

        return recordList;
    }

    private Record createRecordFromEntry(String customTableId, Map<String, Object> params) {
        Record record = new Record();
        record.setCustomTableId(customTableId);

        record.setId((Long) params.get("id"));
        record.setUpdateTime(((Timestamp) params.get("update_time")).toLocalDateTime());
        params.remove("id");
        params.remove("update_time");

        record.setRequisiteValueMap(params);
        return record;
    }

}
