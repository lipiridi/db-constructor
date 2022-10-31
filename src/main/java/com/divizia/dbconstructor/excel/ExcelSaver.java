package com.divizia.dbconstructor.excel;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.service.RequisiteService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class ExcelSaver {

    @Value("${excel.download.max-cells-per-list}")
    private int MAX_CELLS;
    private final RequisiteService requisiteService;

    public Resource exportRecords(String fileName, Map<CustomTable, ? extends Collection<Record>> records) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        records.forEach((x, y) -> {
            XSSFSheet sheet = workbook.createSheet(x.getId());

            Map<String, Integer> requisitePositions;
            if (x.getRequisites() == null)
                requisitePositions = writeHeader(requisiteService.findByCustomTableId(x.getId()), sheet);
            else
                requisitePositions = writeHeader(x.getRequisites(), sheet);

            writeRecords(y, sheet, requisitePositions);
        });

        return getResource(fileName, workbook);
    }

    private Resource getResource(String fileName, XSSFWorkbook workbook) {
        Resource resource;
        try {
            Path tempFile = Files.createTempFile(fileName + "_", ".xlsx");
            OutputStream outputStream = Files.newOutputStream(tempFile);
            workbook.write(outputStream);
            outputStream.close();

            resource = new UrlResource(tempFile.toUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    private void writeRecords(Collection<Record> records, XSSFSheet sheet, Map<String, Integer> requisitePositions) {
        AtomicInteger rowCount = new AtomicInteger(1);

        //I use iterator, because I want to limit file size
        int rows = 0;
        int maxRows = MAX_CELLS / (requisitePositions.size() + 2);
        Iterator<Record> iterator = records.iterator();
        while (iterator.hasNext() && rows < maxRows) {
            Record record = iterator.next();
            Row recordRow = sheet.createRow(rowCount.getAndIncrement());

            Cell cellRecordId = recordRow.createCell(0);
            cellRecordId.setCellValue(record.getId());

            Cell cellRecordLastUpdate = recordRow.createCell(1);
            cellRecordLastUpdate.setCellValue(record.getUpdateTime());

            record.getRequisiteValueMap().forEach((key, value) -> {
                Cell recordCell = recordRow.createCell(requisitePositions.get(key));

                writeCellByType(value, recordCell);
            });

            rows++;
        }
    }

    private void writeCellByType(Object value, Cell recordCell) {
        if (value == null) return;

        if (value instanceof Integer || value instanceof Long || value instanceof Double)
            recordCell.setCellValue(Double.parseDouble(value.toString()));
        else if (value instanceof Boolean)
            recordCell.setCellValue((Boolean) value);
        else if (value instanceof LocalDateTime)
            recordCell.setCellValue((LocalDateTime) value);
        else
            recordCell.setCellValue(String.valueOf(value));
    }

    private Map<String, Integer> writeHeader(Collection<Requisite> requisites, XSSFSheet sheet) {
        HashMap<String, Integer> requisitePositions = new HashMap<>();

        Row row = sheet.createRow(0);

        Cell cellId = row.createCell(0);
        cellId.setCellValue("Id");

        Cell cellLastUpdate = row.createCell(1);
        cellLastUpdate.setCellValue("Last update");

        AtomicInteger cellCount = new AtomicInteger(2);
        requisites.forEach(req -> {
            Cell requisiteName = row.createCell(cellCount.get());
            requisitePositions.put(req.getId(), cellCount.getAndIncrement());
            requisiteName.setCellValue(req.getName());
        });

        return requisitePositions;
    }

}
