package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.client.HTTPClient;
import sample.dto.Discipline;
import sample.dto.EducationalPlan;
import sample.parser.EducationalPlanParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController {


    private Stage authorize;
    private Stage start;
    private Stage check;
    private Stage startWork;

    @FXML
    public ChoiceBox<String> discChoice;
    @FXML
    public ChoiceBox<String> courseChoice;
    @FXML
    public Label version;
    @FXML
    public ChoiceBox<String> choiceBoxEP;
    @FXML
    private TextField login, password;
    @FXML
    private Button closeButton, fileChooser_UP, fileChooser_RP, goBtn;
    @FXML
    private ChoiceBox choiceBox_UP, choiceBox_RP;
    @FXML
    private TextField versionChooser_UP, versionChooser_RP;
    @FXML
    private Label fileName_UP, fileName_RP;
    @FXML
    private Pane pane_UP, pane_RP, pane_Login;
    @FXML
    private JFXButton btn_UP, btn_RP, btn_Login;
    private List<EducationalPlan> plans;

    @FXML
    public void authorize() {
        if (authorize == null) {
            Parent parent = null;
            HTTPClient client = new HTTPClient();
            boolean success = client.authtorize(login.getText(), password.getText());
            if (success)
                pane_UP.toFront();
        }

    }

    @FXML
    public void startScene() {
        if (start == null) {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            start = new Stage(StageStyle.DECORATED);
            start.setScene(new Scene(parent));
        }
        close();
        start.show();

    }

    @FXML
    public void toParseAction() throws InterruptedException, IOException {
        if (!version.isVisible()) {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getClassLoader().getResource("check.fxml"));
                check = new Stage(StageStyle.DECORATED);
                check.setScene(new Scene(parent));
                EducationalPlanParser parser = new EducationalPlanParser();
                EducationalPlan educationalPlan = parser.parse(new URL("file:/" + fileName_UP.getText()));
                HTTPClient client = new HTTPClient();
                client.sendEducationalPlan(educationalPlan);
            } catch (Exception e) {
                e.printStackTrace();
            }
            close();
            check.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("discipline.fxml"));
            Parent root = loader.load();

            DisciplineController disciplineController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Выберите дисциплину и курс");
            stage.setScene(new Scene(root, 300, 250));
            stage.show();

            EducationalPlan educationalPlan = plans.stream()
                    .filter(p -> p.getCustomName().equals(choiceBoxEP.getValue()))
                    .findFirst().orElseThrow(NullPointerException::new);
            disciplineController.setDisciplines(educationalPlan);

        }

    }


    @FXML
    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    public void choice() {
        if (choiceBox_UP.getValue().toString().toLowerCase().equals("с компьютера")) {
            fileChooser_UP.setVisible(true);
            fileName_UP.setVisible(true);
            choiceBoxEP.setVisible(false);
            version.setVisible(false);
        } else {
            HTTPClient client = new HTTPClient();
            plans = client.getAllEducationalPlans();
            List<String> customNames = plans.stream()
                    .map(EducationalPlan::getCustomName)
                    .collect(Collectors.toList());
            choiceBoxEP.getItems().setAll(customNames);
            fileChooser_UP.setVisible(false);
            fileName_UP.setVisible(false);
            choiceBoxEP.setVisible(true);
            version.setVisible(true);
        }
    }

    @FXML
    public void chooser() {

        Stage stage = (Stage) fileChooser_UP.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        fileName_UP.setText(String.valueOf(selectedFile));

    }


    @FXML
    public void buttonSelector(ActionEvent event) {
        if (event.getSource() == btn_UP) {
            pane_UP.toFront();
        } else if (event.getSource() == btn_RP) {
            pane_RP.toFront();
        } else if (event.getSource() == btn_Login) {
            pane_Login.toFront();
        }

    }

    public void fill(ActionEvent actionEvent) {
        EducationalPlan educationalPlan = plans.stream()
                .filter(plan -> plan.getCustomName().equals(choiceBoxEP.getValue().toString()))
                .findFirst().orElseThrow(RuntimeException::new);
        discChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            Set<String> courses = educationalPlan.getDisciplines().stream()
                    .filter(discipline -> discipline.getName().contains(newValue.toString()))
                    .map(Discipline::getCourse)
                    .collect(Collectors.toSet());
            courseChoice.getItems().setAll(courses);
        });

    }
}
