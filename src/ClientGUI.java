import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class ClientGUI extends Application {


    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        primaryStage.setScene(new Scene(grid,600,700));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
