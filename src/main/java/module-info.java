module com.este.promptmaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires imgscalr.lib;


    opens com.este.promptmaker to javafx.fxml;
    exports com.este.promptmaker;
}