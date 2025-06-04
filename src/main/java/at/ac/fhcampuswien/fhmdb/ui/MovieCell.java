package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;

public class MovieCell extends ListCell<Movie> {
    private static final String ADD_LABEL = "Add to Watchlist";
    private static final String REMOVE_LABEL = "Remove";

    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();

    private final JFXButton toggleDetailsBtn = new JFXButton("Show Details");
    private final JFXButton actionBtn = new JFXButton();
    private final HBox buttonBox = new HBox(10, toggleDetailsBtn, actionBtn);

    private final VBox mainContent = new VBox(10);
    private final VBox detailsBox = new VBox(5);

    private final ClickEventHandler<Movie> clickHandler;
    private final String buttonLabel;
    private boolean detailsVisible = false;

    public MovieCell(ClickEventHandler<Movie> clickHandler, String buttonLabel) {
        super();
        this.clickHandler = clickHandler;
        this.buttonLabel = buttonLabel;
        configureButtons();
        configureLabels();
        configureLayout();
    }

    private void configureButtons() {
        actionBtn.setText(buttonLabel);
        if (ADD_LABEL.equalsIgnoreCase(buttonLabel)) {
            actionBtn.getStyleClass().add("background-green");
        } else if (REMOVE_LABEL.equalsIgnoreCase(buttonLabel)) {
            actionBtn.getStyleClass().add("background-red");
        }
        actionBtn.setOnMouseClicked(_ -> {
            if (clickHandler != null && getItem() != null) {
                clickHandler.onClick(getItem());
            }
        });

        toggleDetailsBtn.getStyleClass().add("background-yellow");
        toggleDetailsBtn.setOnAction(_ -> toggleDetails());
    }

    private void configureLabels() {
        title.getStyleClass().add("text-yellow");
        description.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic;");

        wrapLabel(description);
        wrapLabel(genre);
    }

    private void configureLayout() {
        buttonBox.getStyleClass().add("movie-cell-buttons");
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        mainContent.getStyleClass().addAll("background-light-black");
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setPadding(new Insets(10));
        mainContent.getChildren().addAll(title, description, genre, buttonBox);
    }

    private void wrapLabel(Label lbl) {
        lbl.setWrapText(true);
    }

    private void toggleDetails() {
        if (detailsVisible) {
            mainContent.getChildren().remove(detailsBox);
            toggleDetailsBtn.setText("Show Details");
        } else {
            populateDetails();
            if (!mainContent.getChildren().contains(detailsBox)) {
                mainContent.getChildren().add(detailsBox);
            }
            toggleDetailsBtn.setText("Hide Details");
        }
        detailsVisible = !detailsVisible;
    }

    private void populateDetails() {
        detailsBox.getChildren().clear();
        Movie m = getItem();
        if (m == null) return;

        detailsBox.getChildren().addAll(
                createDetail("Release Year: " + m.getReleaseYear()),
                createDetail("Length: " + m.getLengthInMinutes() + " minutes"),
                createDetail("Rating: " + m.getRating() + "/10")
        );
    }

    private Label createDetail(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("text-white");
        wrapLabel(lbl);
        return lbl;
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);
        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            title.setText(movie.getTitle());
            description.setText(
                    movie.getDescription() != null ? movie.getDescription() : "No description available"
            );
            genre.setText(
                    movie.getGenres() != null && !movie.getGenres().isEmpty()
                            ? String.join(", ", movie.getGenres().stream().map(Enum::name).toList())
                            : "No genres available"
            );

            mainContent.getChildren().remove(detailsBox);
            detailsBox.getChildren().clear();
            toggleDetailsBtn.setText("Show Details");
            detailsVisible = false;

            setGraphic(mainContent);
        }
    }
}