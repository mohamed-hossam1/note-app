module com.sourcepackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires javafx.swing;

    exports com.sourcepackage.ViewPackage.LoginView;
    opens com.sourcepackage.ViewPackage.LoginView to javafx.fxml;
    exports com.sourcepackage.ViewPackage.SignUpView;
    opens com.sourcepackage.ViewPackage.SignUpView to javafx.fxml;
    exports com.sourcepackage.ViewPackage.HomeView;
    opens com.sourcepackage.ViewPackage.HomeView to javafx.fxml;
    exports com.sourcepackage.ViewPackage.AddNodeView;
    opens com.sourcepackage.ViewPackage.AddNodeView to javafx.fxml;
    exports com.sourcepackage.ViewPackage.NoteView;
    opens com.sourcepackage.ViewPackage.NoteView to javafx.fxml;


}

