import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;

public class ClientGUI extends Application {
    private final Label messageLabel = new Label("");
    public Template currTemplate;
    private GridPane votingScreenPane;
    private boolean templateActive = false;

    ObjectInputStream in;
    ObjectOutputStream out;

    @Override
    public void start(Stage primaryStage) {
        votingScreenPane = new GridPane();

        Label statusLabel = new Label("Waiting for template");
        votingScreenPane.add(statusLabel, 0, 0);

        int WIDTH = 600;
        int HEIGHT = 350;
        Scene machineWindow = new Scene(votingScreenPane, WIDTH, HEIGHT);

        primaryStage.setTitle("Voting Machine");
        primaryStage.setScene(machineWindow);
        primaryStage.show();

        new Thread(this::receive).start();

    }

    private void receive() {
        try {
            Socket socket = new Socket("localhost", 2121);
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            Object obj = in.readObject();
            if (obj instanceof Template) {
                //changing this stuff.
                currTemplate = (Template) obj;
                //Open interactive windows
                Platform.runLater(this::failWindow);
                Platform.runLater(this::idCardWindow);
            }
        } catch (IOException | ClassNotFoundException e) {
            Platform.runLater(() -> messageLabel.setText("Error: " + e.getMessage()));
        }

    }

    //NEW STUFF
    private void displayTemplate(Template screen) {
        votingScreenPane.getChildren().clear();
        votingScreenPane.setPrefHeight(500);

        votingScreenPane.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
        )));

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(10);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(70);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(10);

        votingScreenPane.getRowConstraints().addAll(row1, row2, row3, row4);

        //TITLE!
        Label titleLabel = new Label(screen.title);
        titleLabel.setPrefWidth(500);
        titleLabel.setAlignment(Pos.CENTER);
        //titleLabel.setPadding(new Insets(0,100,0,225));
        //DESCRIPTION
        Label descriptionLabel = new Label(screen.description);
        descriptionLabel.setPrefWidth(500);
        descriptionLabel.setAlignment(Pos.CENTER);
        //descriptionLabel.setPadding(new Insets(0,100,0,150));

        //RADIO BUTTONS FOR CANDIDATE SELECTIONS
        VBox selectionsBox = new VBox(10);
        selectionsBox.setPrefWidth(500);
        selectionsBox.setAlignment(Pos.CENTER);
        for (String option: screen.selections) {
            RadioButton candidate = new RadioButton(option);
            candidate.setPrefWidth(100);
            selectionsBox.getChildren().add(candidate);
        }

        //TODO: back button functionality
        HBox buttonsBox = new HBox(168);
        if(screen.buttons[0]) {
            Button backbutton = new Button("Back");
            buttonsBox.getChildren().add(backbutton);
            //backButton when clicked handle the back or sum
        }
        //TODO: submit button
        if(screen.buttons[1]) {
            Button submitButton = new Button("Submit");
            buttonsBox.getChildren().add(submitButton);
            //root.add(submitButton, 2, 4);
            //backButton when clicked handle the back or sum
        }

        //TODO: NEXT button
        if(screen.buttons[2]) {
            Button nextButton = new Button("Next");
            buttonsBox.getChildren().add(nextButton);
            //nextButton when clicked handle the next or sum
        }
        //add everything
        descriptionLabel.setPrefWidth(1000);
        votingScreenPane.add(titleLabel, 1, 0);
        votingScreenPane.add(descriptionLabel, 1, 1);
        votingScreenPane.add(selectionsBox, 1, 2);
        votingScreenPane.add(buttonsBox, 1, 3);

    }

    private void idCardWindow() {
        Stage card_dialog = new Stage();
        card_dialog.setTitle("ID Card Reader");

        Label promptLabel = new Label("Type card code:");
        TextField inputField = new TextField();
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox layout = setupIdReaderButtons(inputField, errorLabel, promptLabel);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(300);

        card_dialog.setScene(new Scene(layout));

        card_dialog.show();
    }

    private VBox setupIdReaderButtons(TextField inputField, Label errorLabel, Label promptLabel) {
        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);  // Pressing Enter activates this
        okButton.setOnAction(_ -> {
            String input = inputField.getText().trim();
            if (templateActive) {
                errorLabel.setText("Template is already active!");
            }
            else if (input.isEmpty()) {
                errorLabel.setText("Input cannot be empty!");
            } else {
                System.out.println("User entered: " + input);
                try {
                    displayTemplate(currTemplate);
                    templateActive = true;
                    errorLabel.setText("");
                } catch (Exception e) {
                    System.err.println("Error displaying template: " + e.getMessage());
                }
            }
        });

        return new VBox(10, promptLabel, inputField, errorLabel, okButton);
    }

    private void failWindow() {
        Stage failDialog = new Stage();
        failDialog.setTitle("Fail a Device");

        Label promptLabel = new Label("Select a device to fail:");
        ComboBox<String> deviceDropdown = new ComboBox<>();
        deviceDropdown.getItems().addAll(
                "Printer",
                "SDCard",
                "Latch",
                "Battery",
                "Screen",
                "IDCard"
        );
        deviceDropdown.setPromptText("Choose device");

        VBox layout = setupFailWindowButtons(deviceDropdown, promptLabel);
        failDialog.setScene(new Scene(layout));
        failDialog.show();
    }

    private VBox setupFailWindowButtons
            (ComboBox<String> deviceDropdown, Label promptLabel) {
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button okButton = new Button("Fail!");
        okButton.setDefaultButton(true);
        okButton.setOnAction(_ -> {
            String selected = deviceDropdown.getValue();
            if (selected == null || selected.trim().isEmpty()) {
                errorLabel.setText("Please select a device first.");
            } else {
                errorLabel.setText("");
                handleFail(selected);
            }
        });

        VBox layout = new VBox(10, promptLabel, deviceDropdown, errorLabel, okButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setPrefWidth(300);
        return layout;
    }

    //for failing button(fail a device)
    private void handleFail(String device) {
        //fail a device
        Command cmd;
        switch (device.toLowerCase()) {
            case "printer":
                cmd = new Command(DeviceType.PRINTER, "simulateFailure", null);
                break;
            case "sdcard":
                cmd = new Command(DeviceType.SDCARD, "simulateFailure", null);
                break;
            case "latch":
                cmd = new Command(DeviceType.LATCH, "simulateFailure", null);
                break;
            case "battery":
                cmd = new Command(DeviceType.BATTERY, "simulateFailure", null);
                break;
            case "screen":
                cmd = new Command(DeviceType.SCREEN, "simulateFailure", null);
                break;
            case "idcard":
                cmd = new Command(DeviceType.IDCARD, "simulateFailure", null);
                break;
            default:
                return;
        }
        try {
            out.writeObject(cmd);
            out.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //ALERT SHOWING THE ALERT
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NOTIFICATION");
        alert.setHeaderText("FAULT");
        alert.setContentText(device.toUpperCase(Locale.ROOT) + " FAILED. . .");
        alert.showAndWait();

    }
}
