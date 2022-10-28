package com.divizia.dbconstructor.excel;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

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

public class ExcelSaver {

    private static final int MAX_ROWS = 100;

    public static Resource exportRecords(String fileName, Collection<Requisite> requisites, Collection<Record> records) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(fileName);

        Map<String, Integer> requisitePositions = writeHeader(requisites, sheet);

        writeRecords(records, sheet, requisitePositions);

        return getResource(fileName, workbook);
    }

    private static Resource getResource(String fileName, XSSFWorkbook workbook) {
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

    private static void writeRecords(Collection<Record> records, XSSFSheet sheet, Map<String, Integer> requisitePositions) {
        AtomicInteger rowCount = new AtomicInteger(1);

        //I use iterator, because I want to limit file size
        int rows = 0;
        Iterator<Record> iterator = records.iterator();
        while (iterator.hasNext() && rows < MAX_ROWS) {
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

    private static void writeCellByType(Object value, Cell recordCell) {
        if (value instanceof Integer || value instanceof Long || value instanceof Double)
            recordCell.setCellValue(Double.parseDouble(value.toString()));
        else if (value instanceof Boolean)
            recordCell.setCellValue((Boolean) value);
        else if (value instanceof LocalDateTime)
            recordCell.setCellValue((LocalDateTime) value);
        else
            recordCell.setCellValue(String.valueOf(value));
    }

    private static Map<String, Integer> writeHeader(Collection<Requisite> requisites, XSSFSheet sheet) {
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
