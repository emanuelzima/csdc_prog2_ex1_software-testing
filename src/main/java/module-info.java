/**
 * Module definition for the FH Movie Database application.
 * This module declares its dependencies and exports/opens packages for use by other modules.
 */
module at.ac.fhcampuswien.fhmdb {
    // JavaFX dependencies
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires com.google.gson;
    requires okhttp3;
    requires transitive com.jfoenix;
    requires java.sql;
    requires transitive org.hibernate.orm.core;
    requires com.h2database;
    requires java.naming;
    requires jakarta.persistence;

    // Open packages for reflection
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson, org.hibernate.orm.core;
    exports at.ac.fhcampuswien.fhmdb.ui;
    exports at.ac.fhcampuswien.fhmdb.exceptions;
    exports at.ac.fhcampuswien.fhmdb.api;
    exports at.ac.fhcampuswien.fhmdb.models.sorting;
    opens at.ac.fhcampuswien.fhmdb.models.sorting to com.google.gson, org.hibernate.orm.core;
    exports at.ac.fhcampuswien.fhmdb.observer;
} 