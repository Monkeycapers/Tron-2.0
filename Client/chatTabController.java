package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONWriter;

import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Evan on 12/19/2016.
 */
public class chatTabController implements Initializable {

    @FXML
    TextArea chatTextArea;

    @FXML
    TextField chatTextField;

    @FXML
    Label chatLabel;

    String name = "";

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        System.out.println("hellow world");
                chatTextField.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                TextField src = ((TextField)(event.getSource()));
                System.out.println("handling: " + src.getText());
                showSetupGui.handleChatMessage(name, src.getText());
                src.setText("");
            }
        });
    }
}