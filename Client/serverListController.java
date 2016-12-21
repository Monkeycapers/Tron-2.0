package Client;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.json.JSONWriter;

import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by S199753733 on 12/19/2016.
 */
public class serverListController implements Initializable {

    //Server List view
    @FXML
    ListView listView;

    //Todo move chatTabPane to showSetupGui
    @FXML
    TabPane chatTabPane;

    //User List view

    @FXML
    ListView userListView;


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        System.out.println("Init Server List Gui");
//        chatTabPane.getTabs().add(getNewTab("Wow!", "Wow"));
//        chatTabPane.getTabs().add(getNewTab("much", "lol"));
//        chatTabPane.getTabs().add(getNewTab("panes", "bup"));

        //userListView.getItems().addAll("User1", "User2", "User3");
        //todo NOT my code (need to change)
        userListView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem pmItem = new MenuItem();
            pmItem.textProperty().bind(Bindings.format("Pm \"%s\"", cell.itemProperty()));
            pmItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to pm a user
            });
            contextMenu.getItems().addAll(pmItem);

            //Commands that need authentication
            if (showSetupGui.client.isHighRank) {
                MenuItem banItem = new MenuItem();
                banItem.textProperty().bind(Bindings.format("Ban \"%s\"", cell.itemProperty()));
                banItem.setOnAction(event -> {
                    String item = cell.getItem();
                    //Send a message to ban a user
                });
                //contextMenu.getItems().addAll(banItem);

                MenuItem kickItem = new MenuItem();
                kickItem.textProperty().bind(Bindings.format("Kick \"%s\"", cell.itemProperty()));
                kickItem.setOnAction(event -> {
                    String item = cell.getItem();
                    //Send a message to kick a user
                });

                MenuItem promoteItem = new MenuItem();
                promoteItem.textProperty().bind(Bindings.format("Promote \"%s\"", cell.itemProperty()));
                promoteItem.setOnAction(event -> {
                    String item = cell.getItem();
                    //Send a message to promote a user
                });

                contextMenu.getItems().addAll(kickItem, banItem, promoteItem);
            }

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
//        generalTextField.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println(event.getSource());
//                System.out.println("Text field event");
//            }
//        });


    }

    public Tab getNewTab(String name, String displayname) {
        Tab tab = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(showSetupGui.class.getResource("chatComponent.fxml"));
        try {
            tab = fxmlLoader.load();
            tab.setText(displayname);
            ((chatTabController)(fxmlLoader.getController())).name = name;
            showSetupGui.addChatTab(name, fxmlLoader.getController());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }

    public void addNewTab(String name, String displayname) {

        chatTabPane.getTabs().add(getNewTab(name, displayname));
    }

    public void addUser(String text) {
        userListView.getItems().add(text);
    }

    public void removeUser (String text) {
        userListView.getItems().remove(text);
    }

}