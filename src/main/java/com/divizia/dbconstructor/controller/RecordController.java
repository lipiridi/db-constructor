package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.excel.ExcelSaver;
import com.divizia.dbconstructor.exceptions.RecordNotFoundException;
import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.serializers.Formatter;
import com.divizia.dbconstructor.model.serializers.RecordControllerDeserializer;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("records")
@AllArgsConstructor
public class RecordController {

    private final CustomTableService customTableService;
    private final RecordService recordService;
    private final RecordControllerDeserializer recordControllerDeserializer;
    private final ExcelSaver excelSaver;

    @GetMapping("{customTableId}/all")
    public String getAll(@PathVariable String customTableId, Model model) {
        model.addAttribute("records", recordService.findAll(customTableId));
        addStandardData(customTableId, model);
        return "records/all";
    }

    @GetMapping("{customTableId}/create")
    public String getCreate(@PathVariable String customTableId, Model model) {
        Record record = new Record();
        record.setRequisiteValueMap(new HashMap<>());
        model.addAttribute("record", record);
        addStandardData(customTableId, model);

        return "records/create";
    }

    @PostMapping("{customTableId}/create")
    public String postCreate(@PathVariable String customTableId, @RequestParam Map<String, String> params) {
        Record record = recordControllerDeserializer.deserialize(customTableId, params);
        recordService.saveAndFlush(record);

        return String.format("redirect:/records/%s/all", customTableId);
    }

    @GetMapping("{customTableId}/edit/{id}")
    public String getEdit(@PathVariable String customTableId, @PathVariable Long id, Model model) {
        Record record = recordService.findById(customTableId, id).orElseThrow(() -> new RecordNotFoundException(customTableId, id));
        model.addAttribute("record", record);
        addStandardData(customTableId, model);

        return "records/edit";
    }

    @PostMapping("{customTableId}/edit/{id}")
    public String postEdit(@PathVariable String customTableId, @PathVariable Long id, @RequestParam Map<String, String> params) {
        Record record = recordControllerDeserializer.deserialize(customTableId, params);
        recordService.saveAndFlush(record);

        return String.format("redirect:/records/%s/all", customTableId);
    }

    private void addStandardData(String customTableId, Model model) {
        CustomTable customTable = customTableService.findByIdWithRequisites(customTableId).orElseThrow();

        Map<String, List<Record>> requisitesRecordsMap = new HashMap<>();
        customTable.getRequisites().forEach(x -> {
            if (x.getType() == RequisiteType.FOREIGN)
                requisitesRecordsMap.put(x.getId(), recordService.findAll(x.getForeignTableId()));
        });

        model.addAttribute("requisites", customTable.getRequisites());
        model.addAttribute("requisitesRecordsMap", requisitesRecordsMap);
        model.addAttribute("customTable", customTable);
        model.addAttribute("formatNormal", Formatter.formatNormal);
        model.addAttribute("formatISO", Formatter.formatISO);
    }

    @PostMapping("{customTableId}/delete/{id}")
    public String postDelete(@PathVariable String customTableId, @PathVariable Long id) {
        recordService.deleteById(customTableId, id);
        return String.format("redirect:/records/%s/all", customTableId);
    }

    @GetMapping("{customTableId}/export")
    @ResponseBody
    public ResponseEntity<Resource> exportFile(@PathVariable String customTableId) {
        CustomTable customTable = customTableService.findByIdWithRequisites(customTableId).orElseThrow();
        List<Record> records = recordService.findAll(customTableId);
        Resource resource = excelSaver.exportRecords(customTableId, Map.of(customTable, records));

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
