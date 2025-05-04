package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomeController implements Initializable {

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public TextField releaseYearField;

    @FXML
    public TextField ratingField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Genre> genreComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public JFXButton clearFiltersBtn;

    @FXML
    public JFXButton watchlistBtn;

    public List<Movie> allMovies;
    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    public SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell(onAddToWatchlistClicked, "Add to Watchlist"));

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genre.values());

        searchBtn.setOnAction(event -> filterMovies());
        sortBtn.setOnAction(event -> sortMovies());
        clearFiltersBtn.setOnAction(event -> clearFilters());
        watchlistBtn.setOnAction(event -> switchToWatchlistView());
    }

    public void initializeState() {
        try {
            allMovies = MovieAPI.getMovies(null, null, null, null);
        } catch (IOException e) {
            System.err.println("An error happened while loading movies: " + e.getMessage());
        }

        observableMovies.clear();
        if (allMovies != null) {
            observableMovies.addAll(allMovies);
        }

        sortedState = SortedState.NONE;
        if (sortBtn != null) {
            sortBtn.setText("Sort (asc)");
        }
    }

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedMovie) -> {
        // TODO: Watchlist-Logik zum Hinzufügen von Filmen implementieren
        System.out.println("Add to watchlist clicked: " + clickedMovie.getTitle());
    };

    public void clearFilters() {
        genreComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        releaseYearField.clear();
        ratingField.clear();
        initializeState();
    }

    public void filterMovies() {
        String query = searchField.getText();
        Genre genre = genreComboBox.getValue();
        String releaseYear = releaseYearField.getText();
        String rating = ratingField.getText();
        applyAllFilters(query, genre, releaseYear, rating);
    }

    public void applyAllFilters(String query, Genre genre, String releaseYear, String rating) {
        try {
            observableMovies.setAll(MovieAPI.getMovies(
                    query,
                    genre != null ? genre.name() : null,
                    releaseYear,
                    rating
            ));
        } catch (IOException e) {
            System.err.println("An error happened while loading movies: " + e.getMessage());
        }
    }

    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
            sortBtn.setText("Sort (desc)");
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
            sortBtn.setText("Sort (asc)");
        }
    }

    public void setMovieList(List<Movie> movies) {
        if (movies == null) {
            throw new IllegalArgumentException("Movie list cannot be null");
        }
        this.allMovies = movies;
        observableMovies.setAll(movies);
    }

    public static String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    public static long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    private void switchToWatchlistView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) watchlistBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load watchlist-view.fxml: " + e.getMessage());
        }
    }
}
