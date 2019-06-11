package sample.parser;

import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import sample.dto.Competition;
import sample.dto.ControlForm;
import sample.dto.Discipline;
import sample.dto.EducationalPlan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class EducationalPlanParser {

    private String DIRECTION = "Направление";
    private String PROFILE = "Профиль";
    private String FORM = "Форма обучения:";
    private String PLAN = "Квалификация: ";
    private String DEPARTMENT = "Кафедра";

    private Workbook workbook;

    public Workbook open(URL filename) throws URISyntaxException, IOException {
        File file = new File(filename.toURI());
        FileInputStream inputStream = new FileInputStream(file);
        return new HSSFWorkbook(inputStream);
    }

    public EducationalPlan parse(URL filename) throws IOException, URISyntaxException {
        workbook = open(filename);
        EducationalPlan result = new EducationalPlan();

        Sheet title = workbook.getSheetAt(0);
        result = parseTitleList(title);

        Set<Discipline> disciplines = new HashSet<Discipline>();

        for (Sheet sheet : workbook) {
            if (sheet.getSheetName().matches("Курс *\\d")) {
                disciplines.addAll(parseDisciplines(sheet));
            }
        }
        result.setDisciplines(disciplines);
        return result;
    }

    private Set<Discipline> parseDisciplines(Sheet course) {
        Set<Discipline> disciplines = new HashSet<>();
        String courseStr = String.valueOf(Integer.parseInt(course.getSheetName().replaceAll("[\\D]", "")));
        for (Row row : course) {
            try {
                if (row.getCell(0).getNumericCellValue() > 0) {
                    Discipline discipline = new Discipline();
                    discipline.setCourse(courseStr);
                    discipline.setCode(row.getCell(2).getStringCellValue());
                    discipline.setName(row.getCell(3).getStringCellValue());
                    discipline.setControlForms(convertToForm(row.getCell(25).getStringCellValue()));
                    discipline.setExam_units(String.valueOf((int)row.getCell(33).getNumericCellValue()));
                    discipline.setLec_hours(String.valueOf((int)row.getCell(28).getNumericCellValue()));
                    discipline.setLab_hours(String.valueOf((int)row.getCell(29).getNumericCellValue()));
                    discipline.setPrc_hours(String.valueOf((int)row.getCell(30).getNumericCellValue()));
                    discipline.setSelf_hours(String.valueOf((int)row.getCell(31).getNumericCellValue()));
                    Set<Competition> competitions = fetchCompetitions(discipline.getCode());
                    discipline.setCompetitions(competitions);
                    disciplines.add(discipline);
                }
            } catch (Exception e) {

            }
        }
        return disciplines;
    }

    private Set<Competition> fetchCompetitions(String disciplineCode) {
        Sheet competitionsSheet = workbook.getSheetAt(4);
        Set<Competition> competitionSet = new HashSet<Competition>();
        Competition temp = new Competition();
        for (Row row : competitionsSheet) {
            if (row.getCell(3).getStringCellValue().contains("-")) {
                temp.setCode(row.getCell(3).getStringCellValue());
                temp.setDescription(row.getCell(6).getStringCellValue());
                temp.setType(getCompetitionType(temp.getCode()));
            }
            if (row.getCell(3).getStringCellValue().equals(disciplineCode))
                competitionSet.add(new Competition(
                        temp.getCode(),
                        temp.getDescription(),
                        temp.getType()));
        }
        return competitionSet;
    }

    private String getCompetitionType(String code) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = code.toCharArray();
        for (char letter : chars) {
            if (letter == 'О')
                stringBuilder.append("обще-");
            if (letter == 'П')
                stringBuilder.append("профессиональная");
        }
        String result = stringBuilder.toString();
        if (result.toCharArray()[result.length() - 1] == '-')
            result = result.replace("-", "культурная");
        return result.replace("-", "");
    }

    private Set<ControlForm> convertToForm(String stringForms) {
        String[] controlFormsString = stringForms.split("\\s+");
        Set<ControlForm> controlForms = new HashSet<ControlForm>();
        for (String name : controlFormsString) {
            controlForms.add(new ControlForm(name));
        }
        return controlForms;

    }

    private EducationalPlan parseTitleList(Sheet title) {
        EducationalPlan educationalPlan = new EducationalPlan();
        for (Row row : title) {
            for (Cell cell : row) {
                String cellString = cell.getStringCellValue();
                if (cellString.contains(DIRECTION)) {
                    String tr_dir = cellString.replace(DIRECTION, "").replaceFirst("^ *", "");
                    educationalPlan.setTrDirection(tr_dir);
                } else if (cellString.contains(PROFILE)) {
                    String profile = cellString.replace(PROFILE, "").replace("\"", "").replaceFirst("^ *", "");
                    educationalPlan.setProfile(profile);
                } else if (cellString.contains(FORM)) {
                    String form = cellString.replace(FORM, "").replaceFirst("^ *", "");
                    educationalPlan.setForm(form);
                } else if (cellString.contains(PLAN)) {
                    String plan = cellString.replace(PLAN, "").replaceFirst("^ *", "");
                    educationalPlan.setPlan(plan);
                } else if (cellString.contains(DEPARTMENT)) {
                    Cell cellWithDepartment = row.getCell(cell.getColumnIndex() + 2);
                    educationalPlan.setDepartment(cellWithDepartment.getStringCellValue());
                }
            }
        }
        return educationalPlan;
    }
}
