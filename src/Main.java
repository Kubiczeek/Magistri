import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    int counter = 0; // Example variable to demonstrate state
    private DatabaseConnector databaseConnector;

    public static void main(String[] args) {

        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        databaseConnector = new DatabaseConnector("jdbc:mysql://localhost:3306/magistri", "root", "1234");
        // Initialize the application
        stage.setTitle("Magistři");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        // ChoiceBox for selecting operation
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Add", "Remove", "Update");
        choiceBox.getSelectionModel().select("Add");

        TextField textField_f_name = new TextField();
        textField_f_name.setPromptText("Křestní jméno");
        TextField textField_l_name = new TextField();
        textField_l_name.setPromptText("Příjmení");
        javafx.scene.control.DatePicker datePicker_birth_date = new javafx.scene.control.DatePicker();
        datePicker_birth_date.setPromptText("Datum narození");
        TextField textField_role = new TextField();
        textField_role.setPromptText("Role");
        HBox inputFields = new HBox(10, textField_f_name, textField_l_name, datePicker_birth_date, textField_role);

        ComboBox<String> comboBoxRemove = new ComboBox<>();
        comboBoxRemove.setPromptText("Vyberte uživatele k odebrání");
        comboBoxRemove.setVisible(false);

        Button button = new Button("Submit");

        // Show scene with ChoiceBox and Label
        stage.setScene(new javafx.scene.Scene(root, 400, 300));
        root.getChildren().addAll(choiceBox, inputFields, comboBoxRemove, button);

        choiceBox.setOnAction(ev -> {
            String operation = choiceBox.getValue();
            boolean isRemove = operation.equals("Remove");
            inputFields.setVisible(!isRemove);
            comboBoxRemove.setVisible(isRemove);
            if (isRemove) {
                comboBoxRemove.getItems().clear();
                for (String[] user : databaseConnector.getAllUsers()) {
                    comboBoxRemove.getItems().add(user[0] + " " + user[1] + " (" + user[2] + ", " + user[3] + ")");
                }
            }
        });

        button.setOnAction(e -> {
            String operation = choiceBox.getValue();
            if (operation.equals("Add")) {
                String f_name = textField_f_name.getText();
                String l_name = textField_l_name.getText();
                String birth_date = datePicker_birth_date.getValue() != null ? datePicker_birth_date.getValue().toString() : "";
                String role = textField_role.getText();
                databaseConnector.executeUpdate("INSERT INTO users (first_name, last_name, date_of_birth, role) VALUES (?, ?, ?, ?)",
                        f_name, l_name, birth_date, role);
            } else if (operation.equals("Remove")) {
                String selected = comboBoxRemove.getValue();
                if (selected != null && !selected.isEmpty()) {
                    // Rozparsovat hodnoty
                    String[] parts = selected.split("[() ,]+");
                    String f_name = parts[0];
                    String l_name = parts[1];
                    String birth_date = parts[2];
                    String role = parts[3];
                    databaseConnector.executeUpdate("DELETE FROM users WHERE first_name = ? AND last_name = ? AND date_of_birth = ? AND role = ?",
                        f_name, l_name, birth_date, role);
                    comboBoxRemove.getItems().remove(selected);
                }
            }
        });
    }
}