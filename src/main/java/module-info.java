module com.akinator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.akinator to javafx.fxml;
    exports com.akinator;
}
