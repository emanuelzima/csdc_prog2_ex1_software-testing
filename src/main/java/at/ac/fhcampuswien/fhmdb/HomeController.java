package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.*;
import at.ac.fhcampuswien.fhmdb.models.sorting.SortState;
import at.ac.fhcampuswien.fhmdb.models.sorting.UnsortedState;
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

import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showError;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the main movie list view.
 * This class handles the main functionality of the application including:
 * - Displaying and filtering movies
 * - Sorting movies
 * - Managing the watchlist
 * - Switching between views
 */
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
    private SortState sortState = new UnsortedState();

    /**
     * Initializes the controller and sets up the UI components.
     *
     * @param url            The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(_ -> new MovieCell(onAddToWatchlistClicked, "Add to Watchlist"));

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genre.values());

        searchBtn.setOnAction(_ -> filterMovies());
        sortBtn.setOnAction(_ -> sortMovies());
        clearFiltersBtn.setOnAction(_ -> clearFilters());
        watchlistBtn.setOnAction(_ -> switchToWatchlistView());
    }

    /**
     * Initializes the state of the controller by loading movies from the database or API.
     */
    public void initializeState() {
        try {
            MovieRepository repo = MovieRepository.getInstance();
            List<MovieEntity> cachedMovies = repo.getAllMovies();

            if (cachedMovies.isEmpty()) {
                allMovies = MovieAPI.getMovies(null, null, null, null);
                // Save movies to database
                for (Movie movie : allMovies) {
                    MovieEntity entity = new MovieEntity(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getDescription(),
                            movie.getGenres().stream()
                                    .map(Genre::name)
                                    .collect(Collectors.joining(",")),
                            movie.getReleaseYear(),
                            movie.getImgUrl(),
                            movie.getLengthInMinutes(),
                            movie.getRating()
                    );
                    repo.addMovie(entity);
                }
                System.out.println("Movies loaded from API and cached in database.");
            } else {
                System.out.println("Movies loaded from database.");
                allMovies = cachedMovies.stream()
                        .map(entity -> new Movie(
                                entity.getApiId(),
                                entity.getTitle(),
                                entity.getDescription(),
                                Arrays.stream(entity.getGenres().split(","))
                                        .map(Genre::valueOf)
                                        .collect(Collectors.toList()),
                                entity.getReleaseYear(),
                                entity.getImgUrl(),
                                entity.getLengthInMinutes(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                entity.getRating()
                        ))
                        .collect(Collectors.toList());
            }
        } catch (DatabaseException | MovieApiException e) {
            showError(movieListView.getScene().getWindow(),
                    "Initialization Error",
                    "Failed to load movie data",
                    e.getMessage());
            allMovies = new ArrayList<>();
        }

        observableMovies.clear();
        if (allMovies != null) {
            observableMovies.addAll(allMovies);
        }

        sortState = new UnsortedState();
        if (sortBtn != null) {
            sortBtn.setText(sortState.getButtonLabel());
        }
    }

    /**
     * Event handler for adding a movie to the watchlist.
     */
    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedMovie) -> {
        try {
            WatchlistMovieEntity entity = new WatchlistMovieEntity(clickedMovie.getId());
            WatchlistRepository repo = WatchlistRepository.getInstance();
            repo.addToWatchlist(entity);
            System.out.println(clickedMovie.getTitle() + " was added to watchlist.");
        } catch (DatabaseException e) {
            showError(movieListView.getScene().getWindow(),
                    "Database Error",
                    "Could not add movie to watchlist",
                    e.getMessage());
        }
    };

    /**
     * Clears all filters and resets the movie list.
     */
    public void clearFilters() {
        genreComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        releaseYearField.clear();
        ratingField.clear();
        initializeState();
    }

    /**
     * Filters movies based on the current filter settings.
     */
    public void filterMovies() {
        String query = searchField.getText();
        Genre genre = genreComboBox.getValue();
        String releaseYear = releaseYearField.getText();
        String rating = ratingField.getText();
        applyAllFilters(query, genre, releaseYear, rating);
    }

    /**
     * Applies all filters to the movie list.
     *
     * @param query       Search query
     * @param genre       Selected genre
     * @param releaseYear Release year filter
     * @param rating      Rating filter
     */
    public void applyAllFilters(String query, Genre genre, String releaseYear, String rating) {
        try {
            observableMovies.setAll(MovieAPI.getMovies(
                    query,
                    genre != null ? genre.name() : null,
                    releaseYear,
                    rating
            ));
        } catch (MovieApiException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "API Error",
                    "Failed to load movies",
                    e.getMessage()
            );
        }
    }

    /**
     * Sorts the movie list by title in ascending or descending order.
     */
    public void sortMovies() {
        sortState = sortState.next();
        sortState.sort(observableMovies);
        sortBtn.setText(sortState.getButtonLabel());
    }

    /**
     * Sets the movie list to display.
     *
     * @param movies List of movies to display
     * @throws IllegalArgumentException if movies is null
     */
    public void setMovieList(List<Movie> movies) {
        if (movies == null) {
            throw new IllegalArgumentException("Movie list cannot be null");
        }
        this.allMovies = movies;
        observableMovies.setAll(movies);
    }

    /**
     * Finds the most popular actor in a list of movies.
     *
     * @param movies List of movies to search through
     * @return The name of the most popular actor, or null if no movies are provided
     */
    public static String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Finds the length of the longest movie title in a list of movies.
     *
     * @param movies List of movies to search through
     * @return The length of the longest title, or 0 if no movies are provided
     */
    public static int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    /**
     * Counts the number of movies from a specific director.
     *
     * @param movies   List of movies to search through
     * @param director Name of the director to count movies for
     * @return Number of movies by the specified director
     */
    public static long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    /**
     * Gets movies released between two years.
     *
     * @param movies    List of movies to search through
     * @param startYear Start year (inclusive)
     * @param endYear   End year (inclusive)
     * @return List of movies released between the specified years
     */
    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    /**
     * Switches to the watchlist view.
     */
    private void switchToWatchlistView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) watchlistBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(movieListView.getScene().getWindow(),
                    "Load Error",
                    "Could not open watchlist view",
                    e.getMessage());
        }
    }
}
