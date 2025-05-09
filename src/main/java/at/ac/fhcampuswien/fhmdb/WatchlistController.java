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
import org.h2.store.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showError;

public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXButton homeBtn;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database db = Database.getInstance();
            db.initialize();

            WatchlistRepository watchlistRepo = new WatchlistRepository(db.getWatchlistDao());
            MovieRepository movieRepo = new MovieRepository(db.getMovieDao());

            var apiIds = watchlistRepo.getAllWatchlistApiIds();

            var movieEntities = movieRepo.getMoviesByApiIds(apiIds);

            var movies = MovieEntity.toMovies(movieEntities);

            watchlistMovies.addAll(movies);

            movieListView.setItems(watchlistMovies);
            movieListView.setCellFactory(ListView -> new MovieCell(onRemoveFromWatchlistClicked, "Remove"));
        } catch (DatabaseException e) {
            showError(movieListView.getScene().getWindow(),
                    "Database Error",
                    "Failed to load watchlist",
                    e.getMessage());
        }
        homeBtn.setOnAction(event -> switchToHomeView());
    }

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedMovie) -> {

        try {
            Database db = Database.getInstance();
            db.initialize();

            WatchlistRepository repo = new WatchlistRepository(db.getWatchlistDao());
            int removed = repo.removeFromWatchlist(clickedMovie.getId());

            if (removed > 0) {
                System.out.println(clickedMovie.getTitle() + " wurde von der Watchlist entfernt.");
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
