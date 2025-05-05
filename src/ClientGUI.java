import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientGUI extends Application {
    private ArrayList<Template> templates = new ArrayList<>();
    private int currentIndex;
    private ObjectOutputStream oss;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Voting Machine");

        Label header = setLabel(new Label());
        Label description = setDescription(new Label());
        Label selection = setSelection(new Label());

        Button option1 = setOption(new Button(),"option 1");

        Button option2 = setOption(new Button(),"option 2");
        Button option3 = setOption(new Button(),"option 3");
        Button option4 = setOption(new Button(),"option 4");
        Button option5 = setOption(new Button(),"option 5");


        Button b1 = setButton(new Button(),"Button 1");
        Button b2 = setButton(new Button(),"Button 2");
        Button b3 = setButton(new Button(),"Button 3");

        GridPane grid = new GridPane();
        grid.setPrefSize(600, 600);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setStyle("-fx-background-color: blue;");

        for (int i = 0; i < 6; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        grid.add(header, 1, 0);
        grid.add(description, 1, 1);
        grid.add(selection, 1, 2);
        grid.add(option1, 1, 3);
        grid.add(option2, 1, 4);
        grid.add(option3, 1, 5);
        grid.add(option4, 1, 6);
        grid.add(option5, 1, 7);


        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.getChildren().addAll(b1,b2,b3);


        grid.add(buttons, 1, 8);
        GridPane.setHalignment(header, HPos.CENTER);  // Center the node within the cell

        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
        Thread thread = new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(12345)) {
                System.out.println("Waiting for connection...");
                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("Accepted connection from " + client.getInetAddress().getHostAddress());
                    try{
                        oss = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream os = new ObjectInputStream(client.getInputStream());
                        Object ob = os.readObject();
                        if (ob instanceof ArrayList<?>) {
                            templates =(ArrayList<Template>) ob;
                            currentIndex =0;
                            setTemplate(header,description,selection,option1,option2,option3,option4,option5,b1,b2,b3);
                        }
                        else{
                            System.out.println("fail");
                        }

                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    private void setTemplate(Label header, Label description, Label selection, Button option1, Button option2,
                             Button option3, Button option4, Button option5, Button b1, Button b2, Button b3) {

        Platform.runLater(() -> {
            Template template = templates.getFirst();
            header.setText(template.getTitle());
            description.setText(template.getDescription());

            if(template.getSelections() > 0){
                selection.setText("Please Select: " + template.getSelections());
                selection.setManaged(true);
                selection.setVisible(true);
            }else{
                selection.setManaged(false);
                selection.setVisible(false);
            }


            ArrayList<String> list = new ArrayList<>(template.getOptions().keySet());
            Button[] buttons = {option1,option2,option3,option4,option5};

            for (int i = 0; i < buttons.length; i++) {
                if(i <list.size()){
                    buttons[i].setText(list.get(i));
                    buttons[i].setManaged(true);
                    buttons[i].setVisible(true);
                    buttons[i].setDisable(false);

                    int index = i;
                    buttons[i].setOnAction(e->{
                        buttons[index].setStyle(
                                "-fx-background-color: gray;" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 2px;" +
                                        "-fx-border-radius: 5px;" +
                                        "-fx-background-radius: 5px;" +
                                        "-fx-alignment: center;");
                        template.getOptions().put(list.get(index),true);
                        System.out.println("selected");
                    });
                }else{
                    buttons[i].setManaged(false);
                    buttons[i].setVisible(false);
                    buttons[i].setDisable(true);
                }
            }
            b1.setVisible(currentIndex > 0);
            b1.setOnAction(e ->{
                currentIndex--;
                setTemplate(header,description,selection,option1,option2,option3,option4,option5,b1,b2,b3);
            });
            b3.setVisible(currentIndex < templates.size() -1);
            b3.setOnAction(e ->{
                currentIndex++;
                setTemplate(header,description,selection,option1,option2,option3,option4,option5,b1,b2,b3);
            });
            b2.setVisible(currentIndex == templates.size());
            b2.setOnAction(e ->{
                try {
                    oss.writeObject(templates);
                    oss.flush();
                } catch (IOException ex) {
                    System.err.println("failed to send template");
                }
            });


        });


    }

    public static void main(String[] args) {
        launch(args);

    }

    private Label setLabel(Label header){
        header.setPrefWidth(600);
        header.setText("Proposition Here");
        header.setAlignment(Pos.CENTER);
        header.setFont(Font.font("System", FontWeight.BOLD,24));
        header.setStyle( "-fx-background-color: lightblue;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-alignment: center;");
        return header;
    }

    private Label setDescription(Label description){
        description.setPrefWidth(600);
        description.setPrefHeight(150);
        description.setText("Description of Proposition Here");
        description.setAlignment(Pos.CENTER);
        description.setFont(Font.font("System", FontWeight.BOLD,24));
        description.setStyle( "-fx-background-color: lightyellow;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-alignment: center;");
        return description;
    }
    private Label setSelection(Label selection){
        selection.setPrefWidth(600);
        selection.setText("Number of Selection");
        selection.setAlignment(Pos.CENTER);
        selection.setFont(Font.font("System", FontWeight.BOLD,24));
        selection.setStyle( "-fx-background-color: lightpink;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-alignment: center;");
        return selection;
    }
    private Button setOption(Button option,String text){
        option.setPrefWidth(600);
        option.setText(text);
        option.setAlignment(Pos.CENTER);
        option.setFont(Font.font("System", FontWeight.BOLD,24));
        option.setStyle(
                "-fx-background-color: lightblue;" +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        return option;
    }

    private Button setButton(Button b1,String text){
        b1.setText(text);
        b1.setPrefHeight(50);
        b1.setPrefWidth(150);
        b1.setAlignment(Pos.CENTER);
        b1.setFont(Font.font("System", FontWeight.BOLD,24));
        b1.setStyle(
                "-fx-background-color: lightgreen;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-alignment: center;");
        return b1;
    }
}
