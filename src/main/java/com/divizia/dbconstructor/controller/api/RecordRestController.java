package com.divizia.dbconstructor.controller.api;

import com.divizia.dbconstructor.exceptions.RecordNotFoundException;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/records")
public class RecordRestController {

    private final RecordService recordService;

    RecordRestController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("{customTableId}")
    ResponseEntity<Map<String, Object>> all(@PathVariable String customTableId) {
        customTableId = checkCustomTableId(customTableId);

        Map<String, Object> answer = new HashMap<>();
        answer.put("result", "OK");
        answer.put("entity", recordService.findAll(customTableId));

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @GetMapping("{customTableId}/{id}")
    ResponseEntity<Map<String, Object>> findById(@PathVariable String customTableId, @PathVariable Long id) {
        customTableId = checkCustomTableId(customTableId);

        Map<String, Object> answer = new HashMap<>();
        Optional<Record> optional = recordService.findById(customTableId, id);

        if (optional.isEmpty()) {
            answer.put("result", "Error");
            answer.put("message", new RecordNotFoundException(customTableId, id).getMessage());
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            answer.put("result", "OK");
            answer.put("entity", optional.get());
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }
    }

    @PostMapping("{customTableId}")
    ResponseEntity<Map<String, Object>> save(@PathVariable String customTableId, @Valid @RequestBody Record record, BindingResult result) {
        customTableId = checkCustomTableId(customTableId);
        record.setCustomTableId(customTableId);

        Map<String, Object> answer = new HashMap<>();

        if (result.hasErrors()) {
            answer.put("result", "Error");
            answer.put("message", result.getAllErrors().stream().map(x -> x.getDefaultMessage() + "; ").collect(Collectors.joining()));
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            Optional<Record> foundInDB = recordService.findById(customTableId, record.getId());
            if (foundInDB.isPresent())
                record = foundInDB.get().updateAllowed(record);

            answer.put("result", "OK");
            answer.put("entity", recordService.saveAndFlush(record));
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("{customTableId}/{id}")
    ResponseEntity<Map<String, Object>> delete(@PathVariable String customTableId, @PathVariable Long id) {
        customTableId = checkCustomTableId(customTableId);

        Map<String, Object> answer = new HashMap<>();
        Optional<Record> optional = recordService.findById(customTableId, id);

        if (optional.isEmpty()) {
            answer.put("result", "Error");
            answer.put("message", new RecordNotFoundException(customTableId, id).getMessage());
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            recordService.deleteById(customTableId, id);
            answer.put("result", "OK");
            return new ResponseEntity<>(answer, HttpStatus.ACCEPTED);
        }
    }

    private String checkCustomTableId(String customTableId) {
        return customTableId.startsWith("c_") ? customTableId : "c_" + customTableId;
    }

}
