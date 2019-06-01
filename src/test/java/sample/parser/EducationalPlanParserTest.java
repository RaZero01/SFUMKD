package sample.parser;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import sample.dto.EducationalPlan;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class EducationalPlanParserTest {

    private EducationalPlanParser parser = new EducationalPlanParser();

    @Test
    public void shouldOpenFile() throws IOException, URISyntaxException {
        Workbook wb = parser.open(getClass().getClassLoader().getResource("test.xls"));

        assertNotNull(wb);
    }

    @Test
    public void shouldParseXlsFile() {
/*       EducationalPlan educationalPlan = parser.parse(getClass().getClassLoader().getResource("test.xls"));

       assertNotNull(educationalPlan);
        assertEquals(" Программная инженерия", educationalPlan.);*/
    }
}