package sample.importer;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import sample.dto.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static sample.importer.Keywords.*;

public class WordImporter {

    public static String format = ".docx";
    public static String work_plan = "Рабочая программа_";

    public void importData(EducationalPlan educationalPlan, Discipline discipline) throws IOException, URISyntaxException {
        XWPFDocument document = openTemplate();
        document.getTables()
                .forEach(table -> table.getRows()
                        .forEach(row -> row.getTableCells()
                                .forEach(cell ->
                                        processParagraphs(cell.getParagraphs(), educationalPlan, discipline))));
        processParagraphs(document.getParagraphs(), educationalPlan, discipline);
        save(document, discipline.getCode());
    }

    public void processParagraphs(List<XWPFParagraph> paragraphs, EducationalPlan educationalPlan, Discipline discipline) {
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            Optional.ofNullable(runs).ifPresent(run -> {
                for (XWPFRun r : run) {
                    String text = r.getText(0);
                    Optional.ofNullable(text).ifPresent(txt -> {
                        if (txt.contains(DIRECTION)) {
                            txt = txt.replace(DIRECTION, educationalPlan.getTrDirection());
                        } else if (txt.contains(WHITESPACE)) {
                            txt = txt.replace(WHITESPACE, "     ");
                            CTShd ctShd = r.getCTR().addNewRPr().addNewShd();
                            ctShd.setVal(STShd.CLEAR);
                            ctShd.setColor("auto");
                            ctShd.setFill("00FFFF");
                        } else if (txt.contains(NAME)) {
                            txt = txt.replace(NAME, discipline.getName());
                        } else if (txt.contains(PROFILE)) {
                            txt = txt.replace(PROFILE, educationalPlan.getProfile());
                        } else if (txt.contains(FORM)) {
                            txt = txt.replace(FORM, educationalPlan.getForm());
                        } else if (txt.contains(PLAN)) {
                            txt = txt.replace(PLAN, educationalPlan.getPlan());
                        } else if (txt.contains(DEGREE)) {
                            txt = txt.replace(DEGREE, User.user.getDegree());
                        } else if (txt.contains(USERNAME)) {
                            txt = txt.replace(USERNAME, User.user.getSurname() + " " + User.user.getName().charAt(0) + "."
                                    + " " + User.user.getPatronymic().charAt(0));
                        } else if (txt.contains(COMPETITIONS_ENUM)) {
                            txt = txt.replace(COMPETITIONS_ENUM, getCompetitionEnum(discipline.getCompetitions()));
                        } else if (txt.contains(COMPETITIONS_DESC)) {
                            txt = txt.replace(COMPETITIONS_DESC, getCompetitionDesc(discipline.getCompetitions()));
                        } else if (txt.contains(EXAM_UNITS)) {
                            txt = txt.replace(EXAM_UNITS, discipline.getExam_units());
                        } else if (txt.contains(TOTAL_HOURS)) {
                            txt = txt.replace(TOTAL_HOURS, getTotalCount(discipline));
                        } else if (txt.contains(LC_HOURS)) {
                            txt = txt.replace(LC_HOURS, discipline.getLec_hours());
                        } else if (txt.contains(PRC_HOURS)) {
                            txt = txt.replace(PRC_HOURS, discipline.getPrc_hours());
                        } else if (txt.contains(LB_HOURS)) {
                            txt = txt.replace(LB_HOURS, discipline.getLab_hours());
                        } else if (txt.contains(SELF_HOURS)) {
                            txt = txt.replace(SELF_HOURS, discipline.getSelf_hours());
                        } else if (txt.contains(CONTROL_FORMS)) {
                            txt = txt.replace(CONTROL_FORMS, getControlForms(discipline.getControlForms()));
                        } else if (txt.contains(COMPETITION_ARR)) {
                            txt = txt.replace(COMPETITION_ARR, getCompetitionArr(discipline.getCompetitions()));
                        }
                        r.setText(txt, 0);
                    });
                }

            });

        }
    }

    private String getCompetitionArr(Set<Competition> competitions) {
        return "";

    }

    private String getControlForms(Set<ControlForm> controlForms) {
        StringBuffer str = new StringBuffer();
        controlForms.stream()
                .map(ControlForm::getName)
                .forEach(name -> str.append(name).append(" "));
        return str.toString();
    }

    private String getTotalCount(Discipline discipline) {
        Integer lab = Integer.valueOf(discipline.getLab_hours());
        Integer lec = Integer.valueOf(discipline.getLec_hours());
        Integer prc = Integer.valueOf(discipline.getPrc_hours());
        return String.valueOf(lab + lec + prc);
    }

    private String getCompetitionDesc(Set<Competition> competitions) {
        StringBuffer str = new StringBuffer();
        competitions
                .forEach(code -> str.append(code.getCode()).append(" - ").append(code.getDescription()));
        return str.toString();

    }

    private String getCompetitionEnum(Set<Competition> competitions) {
        StringBuffer str = new StringBuffer();
        competitions.stream()
                .map(Competition::getCode)
                .forEach(code -> str.append(code).append(" "));
        return str.toString();

    }

    public XWPFDocument openTemplate() throws URISyntaxException, IOException {
        File file = new File(getClass().getClassLoader().getResource("template.docx").toURI());
        FileInputStream inputStream = new FileInputStream(file);
        return new XWPFDocument(inputStream);
    }

    public void save(XWPFDocument document, String code) throws IOException {
        FileOutputStream out = new FileOutputStream(work_plan + code + format);
        document.write(out);
        out.close();
        document.close();
    }

}
