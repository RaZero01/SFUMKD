package sample.parser;

import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

@NoArgsConstructor
public class EducationalPlanParser {

    public Workbook open(URL filename) throws URISyntaxException, IOException {
        File file = new File(filename.toURI());
        FileInputStream inputStream = new FileInputStream(file);
        return new HSSFWorkbook(inputStream);
    }
}
