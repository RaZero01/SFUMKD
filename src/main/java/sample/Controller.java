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

import java.io.File;
import java.io.IOException;

public class Controller {


    private Stage authorize;
    private Stage start;
    private Stage check;
    private Stage startWork;

    @FXML
    public Label version;
    @FXML
    public ChoiceBox choiceBoxEP;
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
    public void toParseAction() {
        if (check == null) {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getClassLoader().getResource("check.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            check = new Stage(StageStyle.DECORATED);
            check.setScene(new Scene(parent));
        }
        close();
        check.show();

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
            client.getAllEducationalPlans();
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


}
