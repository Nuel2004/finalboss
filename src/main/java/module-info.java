module com.rodasfiti {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.rodasfiti to javafx.fxml;
    opens com.rodasfiti.controllers to javafx.fxml;

    exports com.rodasfiti;
}
