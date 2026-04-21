module com.socketsexample {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.socketsexample to javafx.fxml;   
    opens com.socketsexample.Modelo to javafx.fxml;
    exports com.socketsexample.Modelo; 
    exports com.socketsexample;
}
