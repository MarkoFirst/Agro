package classes;

import javafx.stage.Stage;

public class MyStage extends Stage{
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private static Stage stage = new Stage();
}
