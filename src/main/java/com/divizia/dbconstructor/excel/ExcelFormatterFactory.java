package com.divizia.dbconstructor.excel;

import com.divizia.dbconstructor.model.enums.RequisiteType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

public class ExcelFormatterFactory {
    private final Map<RequisiteType, XSSFCellStyle> styles;
    private final XSSFWorkbook workbook;

    public ExcelFormatterFactory(XSSFWorkbook workbook) {
        this.styles = new HashMap<>();
        this.workbook = workbook;
    }

    public XSSFCellStyle getStyle(RequisiteType requisiteType) {
        if (!styles.containsKey(requisiteType)) {
            XSSFCellStyle dateStyle = workbook.createCellStyle();

            if (requisiteType == RequisiteType.LOCAL_DATE_TIME)
                setLocalDateTimeFormat(dateStyle);

            styles.put(requisiteType, dateStyle);
        }

        return styles.get(requisiteType);
    }

    private void setLocalDateTimeFormat(XSSFCellStyle dateStyle) {
        dateStyle.setDataFormat(
                workbook.createDataFormat().getFormat("dd.MM.yyyy HH:mm:ss")
        );
    }
}
