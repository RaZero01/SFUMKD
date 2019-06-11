package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import sample.dto.Discipline;
import sample.dto.EducationalPlan;
import sample.importer.WordImporter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class DisciplineController implements Initializable {


    @FXML
    public ChoiceBox<String> discChoice;
    @FXML
    public ChoiceBox<String> courseChoice;

    private EducationalPlan currentPlan;


    public void setDisciplines(EducationalPlan plan) {
        ObservableList<String> disciplineNames = FXCollections.observableArrayList(plan.getDisciplines().stream()
                .map(Discipline::getName)
                .collect(Collectors.toSet()));
        currentPlan = plan;
        discChoice.setItems(disciplineNames);
        discChoice.getSelectionModel().selectedIndexProperty().addListener((observableValue, n1, n2) ->
                setCourses(currentPlan, discChoice.getItems().get((Integer) n2)));
    }

    private void setCourses(EducationalPlan plan, String disciplineName) {
        ObservableList<String> courses = FXCollections.observableArrayList(plan.getDisciplines().stream()
                .filter(discipline -> discipline.getName().equals(disciplineName))
                .map(Discipline::getCourse)
                .collect(Collectors.toSet()));
        courseChoice.setItems(courses.sorted());

    }

    public void importData() throws IOException, URISyntaxException {
        String disciplineName = discChoice.getValue();
        String course = courseChoice.getValue();

        Discipline discipline = currentPlan.getDisciplines().stream()
                .filter(d -> d.getName().equals(disciplineName))
                .filter(d -> d.getCourse().equals(course))
                .findFirst()
                .orElseThrow(NullPointerException::new);

        WordImporter wordImporter = new WordImporter();
        wordImporter.importData(currentPlan, discipline);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
