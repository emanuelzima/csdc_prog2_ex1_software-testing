package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
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

public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXButton homeBtn;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieListView.setItems(watchlistMovies);
        movieListView.setCellFactory(listView -> new MovieCell(onRemoveFromWatchlistClicked, "Remove"));

        homeBtn.setOnAction(event -> switchToHomeView());
    }

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedMovie) -> {
        // TODO: Watchlist-Logik zum Entfernen von Filmen hinzufügen.
        System.out.println("Remove from watchlist clicked: " + clickedMovie.getTitle());
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
