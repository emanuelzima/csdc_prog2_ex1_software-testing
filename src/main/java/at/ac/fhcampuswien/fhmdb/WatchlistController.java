package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.*;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showError;

/**
 * Controller class for the watchlist view.
 * This class handles the functionality for displaying and managing the user's watchlist,
 * including removing movies from the watchlist and switching back to the main view.
 */
public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXButton homeBtn;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();

    /**
     * Initializes the controller and loads the watchlist data.
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();
            MovieRepository movieRepo = MovieRepository.getInstance();

            List<WatchlistMovieEntity> watchlistEntities = watchlistRepo.getAllWatchlistMovies();
            List<Movie> movies = watchlistEntities.stream()
                .map(watchlistEntity -> {
                    try {
                        List<MovieEntity> movieEntities = movieRepo.getMoviesByApiId(watchlistEntity.getApiId());
                        if (!movieEntities.isEmpty()) {
                            MovieEntity movieEntity = movieEntities.get(0);
                            return new Movie(
                                movieEntity.getApiId(),
                                movieEntity.getTitle(),
                                movieEntity.getDescription(),
                                Arrays.stream(movieEntity.getGenres().split(","))
                                    .map(Genre::valueOf)
                                    .collect(Collectors.toList()),
                                movieEntity.getReleaseYear(),
                                movieEntity.getImgUrl(),
                                movieEntity.getLengthInMinutes(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                movieEntity.getRating()
                            );
                        }
                    } catch (DatabaseException e) {
                        showError(movieListView.getScene().getWindow(),
                                "Database Error",
                                "Failed to load movie details",
                                e.getMessage());
                    }
                    return null;
                })
                .filter(movie -> movie != null)
                .collect(Collectors.toList());

            watchlistMovies.addAll(movies);
            movieListView.setItems(watchlistMovies);
            movieListView.setCellFactory(_ -> new MovieCell(onRemoveFromWatchlistClicked, "Remove"));
        } catch (DatabaseException e) {
            showError(movieListView.getScene().getWindow(),
                    "Database Error",
                    "Failed to load watchlist",
                    e.getMessage());
        }
        homeBtn.setOnAction(_ -> switchToHomeView());
    }

    /**
     * Event handler for removing a movie from the watchlist.
     */
    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedMovie) -> {
        try {
            WatchlistRepository repo = WatchlistRepository.getInstance();
            List<WatchlistMovieEntity> watchlistEntities = repo.getWatchlistMoviesByApiId(clickedMovie.getId());
            
            if (!watchlistEntities.isEmpty()) {
                for (WatchlistMovieEntity entity : watchlistEntities) {
                    repo.removeFromWatchlist(entity);
                }
                System.out.println(clickedMovie.getTitle() + " was removed from watchlist.");
                watchlistMovies.remove(clickedMovie);
            } else {
                showError(
                    movieListView.getScene().getWindow(),
                    "Remove Error",
                    "Movie not on watchlist",
                    "Could not find " + clickedMovie.getTitle() + " in your watchlist."
                );
            }
        } catch (DatabaseException e) {
            showError(movieListView.getScene().getWindow(),
                    "Database Error",
                    "Failed to remove movie from watchlist",
                    e.getMessage());
        }
    };

    /**
     * Switches back to the main movie list view.
     */
    private void switchToHomeView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(movieListView.getScene().getWindow(),
                    "Load Error",
                    "Could not open home view",
                    e.getMessage());
        }
    }
}
