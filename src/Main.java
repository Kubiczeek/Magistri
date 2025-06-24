import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    int counter = 0; // Example variable to demonstrate state

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the application
        stage.setTitle("JavaFX Test Application");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();

        //Add button
        // Vytvoření JavaFX tlačítka a scény
        Button buttonUp = new Button("+");
        Button buttonDown = new Button("-");
        Button buttonDouble = new Button("x2");
        Button buttonHalf = new Button("/2");

        // Přidat textový štítek ke counteru
        Label textLabel = new Label();
        textLabel.setText("Counter: " + counter);
        buttonUp.setOnAction(_ -> {
            counter++;
            textLabel.setText("Counter: " + counter);
        });
        buttonDown.setOnAction(_ -> {
            counter--;
            textLabel.setText("Counter: " + counter);
        });

        buttonDouble.setOnAction(_ -> {
            counter *= 2;
            textLabel.setText("Counter: " + counter);
        });
        buttonHalf.setOnAction(_ -> {
            counter /= 2;
            textLabel.setText("Counter: " + counter);
        });

        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER);

        pane.setSpacing(10); // 10px mezera mezi tlačítky

        buttonUp.setPrefSize(100, 50);
        buttonDown.setPrefSize(100, 50);

        buttonUp.setStyle("-fx-font-size: 24px; -fx-background-color: lightblue;");
        buttonDown.setStyle("-fx-font-size: 24px; -fx-background-color: lightcoral;");


        pane.getChildren().addAll(buttonHalf, buttonDown, buttonUp, buttonDouble);

        // Vytvoření kontejneru pro tlačítko
        VBox root = new VBox(10); // 10px mezera
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(textLabel, pane);

        // Vytvoření a nastavení scény
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 300, 250);
        stage.setScene(scene);

    }
}