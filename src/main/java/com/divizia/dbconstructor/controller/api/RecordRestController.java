package com.divizia.dbconstructor.controller.api;

import com.divizia.dbconstructor.exceptions.RecordNotFoundException;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.serializers.RecordControllerDeserializer;
import com.divizia.dbconstructor.model.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/records")
public class RecordRestController {

    private final RecordService recordService;
    private final RecordControllerDeserializer recordControllerDeserializer;

    RecordRestController(RecordService recordService, RecordControllerDeserializer recordControllerDeserializer) {
        this.recordService = recordService;
        this.recordControllerDeserializer = recordControllerDeserializer;
    }

    @GetMapping
    ResponseEntity<Map<String, Object>> all(@PathParam("customTableId") String customTableId, @PathParam("id") Long id) {
        if (id != null)
            return findById(customTableId, id);
        customTableId = checkCustomTableId(customTableId);
        if (customTableId == null)
            return nullCustomTableAnswer();

        Map<String, Object> answer = new HashMap<>();
        answer.put("result", "OK");
        answer.put("entity", recordService.findAll(customTableId));

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    ResponseEntity<Map<String, Object>> findById(String customTableId, Long id) {
        customTableId = checkCustomTableId(customTableId);
        if (customTableId == null)
            return nullCustomTableAnswer();

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

    @PostMapping
    ResponseEntity<Map<String, Object>> save(@PathParam("customTableId") String customTableId, @Valid @RequestBody Record record, BindingResult result) {
        customTableId = checkCustomTableId(customTableId);
        if (customTableId == null)
            return nullCustomTableAnswer();

        record = recordControllerDeserializer.checkRequisites(customTableId, record);

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

    @DeleteMapping
    ResponseEntity<Map<String, Object>> delete(@PathParam("customTableId") String customTableId, @PathParam("id") Long id) {
        customTableId = checkCustomTableId(customTableId);
        if (customTableId == null)
            return nullCustomTableAnswer();

        Map<String, Object> answer = new HashMap<>();
        Optional<Record> optional = Optional.empty();
        if (id != null)
            optional = recordService.findById(customTableId, id);

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
        if (customTableId == null) return null;
        return customTableId.startsWith("c_") ? customTableId : "c_" + customTableId;
    }

    private ResponseEntity<Map<String, Object>> nullCustomTableAnswer() {
        Map<String, Object> answer = new HashMap<>();
        answer.put("result", "Error");
        answer.put("message", "You didn't specify a customTableId");
        return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
    }

}
