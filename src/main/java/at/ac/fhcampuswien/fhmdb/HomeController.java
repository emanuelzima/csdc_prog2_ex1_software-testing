package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell());

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genre.values());        // fügt die genres in die combobox hinzu

        // event handlers
//        searchBtn.setOnAction(event -> ); // genre filter fehlt
//        sortBtn.setOnAction(event -> ); // sort methode fehlt
    }

    public void initializeState() {
        observableMovies.clear();
        observableMovies.addAll(allMovies);
        if (sortBtn != null) {
            sortBtn.setText("Sort (asc)");
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (movies == null) {
            throw new IllegalArgumentException("Movie list cannot be null");
        }
        if (query == null || query.trim().isEmpty()) {
            return movies;
        }
        String lowerQuery = query.toLowerCase().trim();
        List<Movie> filtered = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(lowerQuery) || movie.getDescription().toLowerCase().contains(lowerQuery));
            {
                filtered.add(movie);
            }
        }
        return filtered;
    }

}