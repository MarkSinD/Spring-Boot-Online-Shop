package com.shopme.shopmebackend.category.export;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import com.shopme.shopmebackend.user.export.AbstractExporter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class CategoryExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;


    public CategoryExcelExporter(){
        workbook = new XSSFWorkbook();
    }



    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if(value instanceof Boolean)
            cell.setCellValue((Boolean) value);
        else
            cell.setCellValue((String) value);

        cell.setCellStyle(style);

    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("Category");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "Category ID", cellStyle);
        createCell(row, 1, "Name", cellStyle);
        createCell(row, 2, "Alias", cellStyle);
        createCell(row, 3, "Enabled", cellStyle);


    }
    public void export(List<Category> categoryList, HttpServletResponse response) throws IOException {
        // Подключение для установки расширения и типа файла
        super.setResponseHeader(response, "application/octet-stream", ".xlsx");

        // Вставить header's колонок
        writeHeaderLine();
        writeDataLines(categoryList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<Category> categoryList) {
        int rowIndex = 1;
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for( Category category : categoryList){
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, category.getId(), cellStyle);
            createCell(row, columnIndex++, category.getName(), cellStyle);
            createCell(row, columnIndex++, category.getAlias(), cellStyle);
            createCell(row, columnIndex++, category.isEnabled(), cellStyle);
        }
    }

}
