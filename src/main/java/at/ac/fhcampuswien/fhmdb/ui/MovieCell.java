package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final Button actionBtn = new Button();
    private final VBox layout = new VBox();

    public MovieCell(ClickEventHandler<Movie> clickHandler, String buttonLabel) {
        super();

        actionBtn.setText(buttonLabel);
        actionBtn.setOnMouseClicked(mouseEvent -> {
            if (clickHandler != null && getItem() != null) {
                clickHandler.onClick(getItem());
            }
        });

        layout.getChildren().addAll(title, detail, genre, releaseYear, rating, actionBtn);
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        releaseYear.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        detail.setWrapText(true);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            title.setText(movie.getTitle());

            detail.setText(movie.getDescription() != null
                    ? movie.getDescription()
                    : "No description available");

            genre.setText(movie.getGenres() != null
                    ? movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(", "))
                    : "No genres available");

            releaseYear.setText("Release Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + movie.getRating());

            setGraphic(layout);
        }
    }
}
