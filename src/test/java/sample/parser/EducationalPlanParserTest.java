package sample.parser;

import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Sets;
import org.junit.Test;
import sample.dto.Competition;
import sample.dto.ControlForm;
import sample.dto.Discipline;
import sample.dto.EducationalPlan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class EducationalPlanParserTest {

    private EducationalPlanParser parser = new EducationalPlanParser();

    @Test
    public void shouldOpenFile() throws IOException, URISyntaxException {
        Workbook wb = parser.open(getClass().getClassLoader().getResource("test.xls"));

        assertNotNull(wb);
    }

    @Test
    public void shouldParseTitle() throws IOException, URISyntaxException {
        EducationalPlan educationalPlan = parser.parse(getClass().getClassLoader().getResource("test.xls"));
        educationalPlan.setDisciplines(null);

        EducationalPlan expected = EducationalPlan.builder()
                .trDirection("09.03.04 Программная инженерия")
                .profile("Интелектуальные программные системы и комплексы")
                .form("очная")
                .plan("бакалавр")
                .department("Инструментального и прикладного программного обеспечения")
                .build();
        assertNotNull(educationalPlan);
        assertThat(educationalPlan, is(expected));
    }

    @Test
    public void shouldParseDisciplines() throws IOException, URISyntaxException {
        EducationalPlan educationalPlan = parser.parse(getClass().getClassLoader().getResource("test.xls"));

        Discipline discipline = Discipline.builder()
                .code("Б1.Б.1")
                .name("История")
                .competitions(Sets.newLinkedHashSet(
                        new Competition("ОК-2", "способностью анализировать основные этапы и закономерности исторического развития общества для формирования гражданской позиции", "общекультурная"),
                        new Competition("ОК-6", "способностью работать в коллективе, толерантно воспринимать социальные, этнические, конфессиональные и культурные различия", "общекультурная")))
                .controlForms(Collections.singleton(new ControlForm("Экз")))
                .exam_units("3")
                .lec_hours("16")
                .lab_hours("0")
                .prc_hours("16")
                .self_hours("40")
                .course("1")
                .build();
        EducationalPlan expected = EducationalPlan.builder()
                .disciplines(Collections.singleton(discipline))
                .build();

        assertNotNull(educationalPlan);
        assertTrue(educationalPlan.getDisciplines().contains(discipline));

    }
}