module com.socketsexample {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.socketsexample to javafx.fxml;
    opens com.hilos to javafx.fxml;
    exports com.socketsexample;
    exports com.hilos;
}
