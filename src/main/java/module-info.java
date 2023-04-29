module com.este.promptmaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.apache.commons.text;
    requires java.desktop;
    requires imgscalr.lib;

    opens com.este.promptmaker to javafx.fxml;
    exports com.este.promptmaker;
}