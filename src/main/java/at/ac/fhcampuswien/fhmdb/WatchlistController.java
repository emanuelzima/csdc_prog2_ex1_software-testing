package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.*;
import at.ac.fhcampuswien.fhmdb.models.WatchlistStatus;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showError;
import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showInfo;

/**
 * Controller-Klasse für die Watchlist-Ansicht.
 * Implementiert Observer, um Benachrichtigungen vom WatchlistRepository zu erhalten.
 */
public class WatchlistController implements Initializable, Observer {

    @FXML public JFXListView<Movie> movieListView;
    @FXML public JFXButton homeBtn;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();

    /**
     * Initialisiert den Controller, lädt die Watchlist-Filme
     * und meldet sich beim WatchlistRepository als Observer an.
     *
     * @param url            Der Pfad zur FXML-Ressource
     * @param resourceBundle Ressourcen für Internationalisierung
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
                            showError(
                                    movieListView.getScene().getWindow(),
                                    "Database Error",
                                    "Failed to load movie details",
                                    e.getMessage()
                            );
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            watchlistMovies.clear();
            watchlistMovies.addAll(movies);
            movieListView.setItems(watchlistMovies);
            movieListView.setCellFactory(_ -> new MovieCell(onRemoveFromWatchlistClicked, "Remove"));

            watchlistRepo.addObserver(this);

        } catch (DatabaseException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "Database Error",
                    "Failed to load watchlist",
                    e.getMessage()
            );
        }

        homeBtn.setOnAction(_ -> switchToHomeView());
    }

    /**
     * Event-Handler für den "Remove from Watchlist"-Button in jeder MovieCell.
     * Entfernt Filme aus DB und UI. Meldung erfolgt über update(...).
     */
    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = clickedMovie -> {
        try {
            WatchlistRepository repo = WatchlistRepository.getInstance();
            List<WatchlistMovieEntity> watchlistEntities = repo.getWatchlistMoviesByApiId(clickedMovie.getId());

            if (!watchlistEntities.isEmpty()) {
                for (WatchlistMovieEntity entity : watchlistEntities) {
                    repo.removeFromWatchlist(entity);
                }
                watchlistMovies.removeIf(movie -> movie.getId().equals(clickedMovie.getId()));
            } else {
                showError(
                        movieListView.getScene().getWindow(),
                        "Remove Error",
                        "Movie not on watchlist",
                        "Der Film \"" + clickedMovie.getTitle() + "\" ist nicht in deiner Watchlist."
                );
            }
        } catch (DatabaseException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "Database Error",
                    "Failed to remove movie from watchlist",
                    e.getMessage()
            );
        }
    };

    /**
     * Wird aufgerufen, wenn das WatchlistRepository eine Benachrichtigung sendet.
     *
     * @param status Der Typ des Ereignisses (NOT_FOUND, REMOVED_SUCCESS etc.)
     * @param title  Der Titel des betroffenen Films
     */
    @Override
    public void update(WatchlistStatus status, String title) {
        Window parentWindow = movieListView.getScene().getWindow();
        switch (status) {
            case NOT_FOUND:
                showError(
                        parentWindow,
                        "Watchlist Status Info",
                        "Film nicht gefunden",
                        "\"" + title + "\" war nicht in der Watchlist."
                );
                break;
            case REMOVED_SUCCESS:
                showInfo(
                        parentWindow,
                        "Watchlist Status Info",
                        "Film entfernt",
                        "\"" + title + "\" wurde aus der Watchlist entfernt."
                );
                break;
            default:
                break;
        }
    }

    /**
     * Wechselt zurück zur Haupt-Ansicht; meldet davor den aktuellen Controller ab.
     */
    private void switchToHomeView() {
        WatchlistRepository.getInstance().removeObserver(this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "Load Error",
                    "Could not open home view",
                    e.getMessage()
            );
        }
    }
}
