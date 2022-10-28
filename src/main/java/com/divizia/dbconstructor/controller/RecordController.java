package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.exceptions.RecordNotFoundException;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.serializers.Formatter;
import com.divizia.dbconstructor.model.serializers.RecordControllerDeserializer;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RecordService;
import com.divizia.dbconstructor.model.service.RequisiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("records")
public class RecordController {

    private final CustomTableService customTableService;
    private final RequisiteService requisiteService;
    private final RecordService recordService;
    private final RecordControllerDeserializer recordControllerDeserializer;

    public RecordController(CustomTableService customTableService, RequisiteService requisiteService,
                            RecordService recordService, RecordControllerDeserializer recordControllerDeserializer) {
        this.customTableService = customTableService;
        this.requisiteService = requisiteService;
        this.recordService = recordService;
        this.recordControllerDeserializer = recordControllerDeserializer;
    }

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
        List<Requisite> requisites = requisiteService.findByCustomTableId(customTableId);
        Map<String, List<Record>> requisitesRecordsMap = new HashMap<>();
        requisites.forEach(x -> {
            if (x.getType() == RequisiteType.FOREIGN)
                requisitesRecordsMap.put(x.getId(), recordService.findAll(x.getForeignTableId()));
        });

        model.addAttribute("requisites", requisites);
        model.addAttribute("requisitesRecordsMap", requisitesRecordsMap);
        model.addAttribute("customTable", customTableService.findById(customTableId).orElseThrow());
        model.addAttribute("formatNormal", Formatter.formatNormal);
        model.addAttribute("formatISO", Formatter.formatISO);
    }

    @PostMapping("{customTableId}/delete/{id}")
    public String postDelete(@PathVariable String customTableId, @PathVariable Long id) {
        recordService.deleteById(customTableId, id);
        return String.format("redirect:/records/%s/all", customTableId);
    }

}
