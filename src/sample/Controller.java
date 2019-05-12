package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    private Stage authorize;
    private Stage start;
    private Stage check;
    private Stage startWork;

    @FXML
        private Button closeButton;
    @FXML
        private ChoiceBox choiceBox;
    @FXML
        private Button fileChooser;
    @FXML
        private TextField versionChooser;
    @FXML
        private Label fileName;
    @FXML
        private Button goBtn;

    @FXML
    public void authorize(){
        if(authorize == null){
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("loader.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            close();
            authorize = new Stage(StageStyle.DECORATED);
            authorize.setScene(new Scene(parent));

        }

        authorize.show();

    }

    @FXML
    public void startScene(){
        if(start == null){
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("login.fxml"));
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
    public void toParseAction(){
        if(check == null){
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("check.fxml"));
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
    public void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    public void choice(){
        if (choiceBox.getValue().toString().toLowerCase().equals("с компьютера")){
            fileChooser.setVisible(true);
            fileName.setVisible(true);
            versionChooser.setVisible(false);
        } else {
            fileChooser.setVisible(false);
            fileName.setVisible(false);
            versionChooser.setVisible(true);
        }

    }

    @FXML
    public void chooser(){

        Stage stage = (Stage) fileChooser.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        fileName.setText(String.valueOf(selectedFile));

    }

    @FXML
    public void goNext(){
        if(startWork == null){
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("main.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            startWork = new Stage(StageStyle.DECORATED);
            startWork.setScene(new Scene(parent));
        }
        close();
        startWork.show();
    }




}
