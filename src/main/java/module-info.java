module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires com.google.gson;
    requires okhttp3;
    requires com.jfoenix;
    requires java.sql;
    requires ormlite.jdbc;
    requires com.h2database;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml, ormlite.jdbc;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    opens at.ac.fhcampuswien.fhmdb.models to javafx.fxml, com.google.gson;
} 