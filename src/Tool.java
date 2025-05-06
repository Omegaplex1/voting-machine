//package BallotPackage;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.geometry.Pos;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Tool extends Application{
//    private List<NewProp> allProps = new ArrayList<>();
//    static String file = "name";
//    @Override
//    public void start(Stage primaryStage)  {
//
//
//        //Set up the initial Proposition
//        VBox questions = new VBox(20);
//        NewProp newProp = new NewProp();
//        GridPane firstQuestion = newProp.getQues();
//        allProps.add(newProp);
//        questions.getChildren().add(firstQuestion);
//
//        //Put the buttons at the bottom of the window.
//        Button submit = new Button("Submit");
//        Button cancel = new Button("Cancel");
//        Button add = new Button("+");
//        HBox buttons = new HBox(10,submit,cancel,add);
//        buttons.setAlignment(Pos.CENTER);
//        questions.getChildren().add(buttons);
//
//        //Add event handlers to the buttons.
//        cancel.setOnAction(e -> {System.exit(0);});
//        add.setOnAction(e -> {addProp(questions);});
//        submit.setOnAction(e -> {
//            String mark = createMarkup();
//            System.out.println(mark);
//            try {
//                //The output to a file.
//                FileWriter markUp = new FileWriter("resources/" + file + ".txt");
//                markUp.write(mark);
//                markUp.close();
//                System.exit(0);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//
//        //Create Scroll pane
//        ScrollPane scroll = new ScrollPane(questions);
//        scroll.setFitToWidth(true);
//        scroll.setPannable(true);
//
//        //Make the scene
//        Scene scene = new Scene(scroll, 500, 500);
//        primaryStage.setTitle("Mark up Tool");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        //This is the file name for the mark up template.
//        file = args[0];
//        launch(args);
//    }
//
//    /**
//     * Add additional propositions once the + button is pressed.
//     * @param questions
//     */
//
//    private void addProp(VBox questions) {
//        NewProp newProp = new NewProp();
//        questions.getChildren().add(questions.getChildren().size() - 1,newProp.getQues());
//        allProps.add(newProp);
//
//
//    }
//
//    /**
//     * This will create the mark up language
//     * @return A string
//     */
//    private String createMarkup(){
//        StringBuilder full = new StringBuilder();
//        full.append("/b\n");
//        for(NewProp prop : allProps ){
//            full.append(eachProp(prop));
//        }
//        full.append("//b\n");
//        return full.toString();
//    }
//
//    /**
//     * This creates the mark up for each propsosition in the arraylist.
//     * @param prop
//     * @return
//     */
//    private String eachProp(NewProp prop){
//        StringBuilder eachProp = new StringBuilder();
//        String check = "good";
//        //Make sure the upper text boxes are filled.
//        if(!prop.getTitle().isEmpty() || !prop.getDescription().isEmpty() || !prop.getSelections().isEmpty()) {
//            eachProp.append("/p\n");
//            eachProp.append("/t\n").append(prop.getTitle()).append("\n//t\n");
//            eachProp.append("/d\n").append(prop.getDescription()).append("\n//d\n");
//            eachProp.append("/n\n").append(prop.getSelections()).append("\n//n\n");
//            check = prop.getNumOptions();
//            eachProp.append(prop.getNumOptions());
//            eachProp.append("//p\n");
//        }
//        //This is error cheak to make sure all option fields are filled in.
//        if(check.equalsIgnoreCase("None"))
//        {
//            return "";
//        }
//        return eachProp.toString();
//    }
//}
