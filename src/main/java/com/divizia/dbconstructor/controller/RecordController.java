package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.exceptions.RecordNotFoundException;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.serializers.RecordControllerDeserializer;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RecordService;
import com.divizia.dbconstructor.model.service.RequisiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("records")
public class RecordController {

    private final CustomTableService customTableService;
    private final RequisiteService requisiteService;
    private final RecordService recordService;
    private final RecordControllerDeserializer recordControllerDeserializer;
    private final DateTimeFormatter formatNormal = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final DateTimeFormatter formatISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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
        model.addAttribute("requisites", requisiteService.findByCustomTableId(customTableId));
        model.addAttribute("customTable", customTableService.findById(customTableId).orElseThrow());
        model.addAttribute("formatNormal", formatNormal);
        model.addAttribute("formatISO", formatISO);
    }

    @PostMapping("{customTableId}/delete/{id}")
    public String postDelete(@PathVariable String customTableId, @PathVariable Long id) {
        recordService.deleteById(customTableId, id);
        return String.format("redirect:/records/%s/all", customTableId);
    }

}
