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

public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXButton homeBtn;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database db = new Database();
            db.createConnectionsSource();
            db.createTables();
            db.initializeDaos();

            WatchlistRepository watchlistRepo = new WatchlistRepository(db.getWatchlistDao());
            MovieRepository movieRepo = new MovieRepository(db.getMovieDao());

            var apiIds = watchlistRepo.getAllWatchlistApiIds();

            var movieEntities = movieRepo.getMoviesByApiIds(apiIds);

            var movies = MovieEntity.toMovies(movieEntities);

            watchlistMovies.addAll(movies);

            movieListView.setItems(watchlistMovies);
            movieListView.setCellFactory(ListView -> new MovieCell(onRemoveFromWatchlistClicked, "Remove"));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        homeBtn.setOnAction(event -> switchToHomeView());
    }

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedMovie) -> {
        // TODO: Watchlist-Logik zum Entfernen von Filmen hinzufügen.
        try {
            Database db = new Database();
            db.createConnectionsSource();
            db.initializeDaos();

            WatchlistRepository repo = new WatchlistRepository(db.getWatchlistDao());
            int removed = repo.removeFromWatchlist(clickedMovie.getId());

            if (removed > 0) {
                System.out.println(clickedMovie.getTitle() + " wurde von der Watchlist entfernt.");
                watchlistMovies.remove(clickedMovie);
            } else {
                System.out.println("Film nicht auf der Watchlist gefunden.");
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
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
            System.err.println("Could not load home-view.fxml: " + e.getMessage());
        }
    }
}
