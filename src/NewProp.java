//package BallotPackage;
//
//import javafx.geometry.Pos;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class NewProp {
//
//    private GridPane ques = new GridPane();
//    List<TextField> options = new ArrayList<>();
//
//    /**
//     * Constructer for each gridpane for the mark up that has many text fields.
//     */
//    public NewProp(){
//        setProps();
//        setOptions();
//        setEnd();
//    }
//
//    /**
//     * gets the Gridpane of the object.
//     * @return
//     */
//    public GridPane getQues(){
//        return ques;
//    }
//
//    /**
//     * Sets the text fields for the options a max of 5
//     */
//    private void setOptions(){
//        int placement = 4;
//        for (int i = 1; i <= 5; i++) {
//            Label optionLabel = new Label("Enter option " + i + " :");
//            TextField option = new TextField();
//            option.setPrefWidth(300);
//            option.setPrefHeight(25);
//            options.add(option);
//            HBox optionBox = new HBox(80,optionLabel,option);
//            optionBox.setAlignment(Pos.CENTER_LEFT);
//            ques.add(optionBox, 0, placement);
//            placement ++;
//        }
//    }
//
//    /**
//     * Sets uo the title,description and selection textfields.
//     *
//     */
//    private void setProps(){
//
//        //Title
//        Label titleLabel = new Label("Enter Title : ");
//        TextArea title = new TextArea();
//        title.setPrefRowCount(2);
//        title.setWrapText(true);
//        title.setPrefWidth(300);
//        title.setPrefHeight(25);
//        HBox titleBox = new HBox(98,titleLabel,title);
//
//        //Title
//        Label descriptionLabel = new Label("Enter Description : ");
//        TextArea description = new TextArea();
//        description.setPrefRowCount(3);
//        description.setWrapText(true);
//        description.setPrefWidth(300);
//        description.setPrefHeight(25);
//        HBox Desbox = new HBox(60,descriptionLabel,description);
//
//        //Selections
//        Label selectLabel = new Label("Enter Number of Selections :");
//        TextField selections = new TextField();
//        //Only one number between 1 and 5
//        selections.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(!newValue.matches("[1-5]?")){
//                selections.setText(oldValue);
//            }
//        });
//        selections.setPrefWidth(25);
//        selections.setPrefHeight(25);
//        HBox Selbox = new HBox(10,selectLabel,selections);
//
//
//        ques.add(titleBox, 0, 1);
//        ques.add(Desbox, 0, 2);
//        ques.add(Selbox, 0, 3);
//    }
//
//    /**
//     * gets the title String
//     * @return
//     */
//    public String getTitle(){
//         HBox title = (HBox) ques.getChildren().get(0);
//         return ((TextArea) title.getChildren().get(1)).getText();
//    }
//
//    /**
//     * gets the Description Strings
//     * @return
//     */
//    public String getDescription(){
//        HBox description = (HBox) ques.getChildren().get(1);
//        return ((TextArea) description.getChildren().get(1)).getText();
//    }
//
//    /**
//     * gests the Selection String
//     * @return
//     */
//    public String getSelections(){
//        HBox selections = (HBox) ques.getChildren().get(2);
//        return ((TextField) selections.getChildren().get(1)).getText();
//    }
//
//    /**
//     * gets the option Strings
//     * @return
//     */
//    public String getNumOptions(){
//        StringBuilder opts = new StringBuilder();
//        int none = 0;
//        for(TextField option : options){
//            if(!option.getText().isEmpty()) {
//                opts.append("/o").append("\n");
//                opts.append(option.getText()).append("\n//o\n");
//            }else{
//                none++;
//            }
//
//        }//make sure at least one option is flled.
//        if(none == 5){
//            return "None";
//        }
//        return opts.toString();
//    }
//
//    /**
//     * Puts the blue line to show the different propositions.
//     */
//    private void setEnd(){
//        Label line = new Label("-------------------------------------------------------------------------------------------------------------");
//        line.setStyle("-fx-font-size: 32px; -fx-text-fill: blue;");
//        ques.add(line, 0, 9);
//    }
//
//}
