package com.shopme.shopmebackend.category.export;

import com.shopme.common.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class CategoryCsvExporter extends AbstractExporter {
    public void export(List<Category> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category ID", "Name", "Alias", "Enabled"};
        String[] fieldMapping = {"id", "name", "alias", "enabled"};
        csvWriter.writeHeader(csvHeader);

        for( Category category : userList){
            csvWriter.write(category, fieldMapping);
        }
        csvWriter.close();
    }
}
