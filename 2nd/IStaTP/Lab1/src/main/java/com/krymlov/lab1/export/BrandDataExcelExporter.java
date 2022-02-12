package com.krymlov.lab1.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.krymlov.lab1.entity.BrandEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BrandDataExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<BrandEntity> listBrands;

    public BrandDataExcelExporter(List<BrandEntity> listBrands) {
        this.listBrands = listBrands;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Brands");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "NAME", style);
        createCell(row, 2, "INFO", style);
        createCell(row, 3, "COUNTRY", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long){
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (BrandEntity brand : listBrands) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, brand.getId(), style);
            createCell(row, columnCount++, brand.getName(), style);
            createCell(row, columnCount++, brand.getInfo(), style);
            createCell(row, columnCount++, brand.getCountry().getName() + " " + brand.getCountry().getId(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
