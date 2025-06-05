package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.*;
import at.ac.fhcampuswien.fhmdb.models.sorting.SortState;
import at.ac.fhcampuswien.fhmdb.models.sorting.UnsortedState;
import at.ac.fhcampuswien.fhmdb.models.WatchlistStatus;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showError;
import static at.ac.fhcampuswien.fhmdb.ui.AlertUtility.showInfo;

/**
 * Controller-Klasse für die Hauptansicht (Home) mit der Movie-Liste.
 * Implementiert Observer, um Benachrichtigungen vom WatchlistRepository zu erhalten.
 */
public class HomeController implements Initializable, Observer {

    @FXML public JFXButton searchBtn;
    @FXML public TextField searchField;
    @FXML public TextField releaseYearField;
    @FXML public TextField ratingField;
    @FXML public JFXListView<Movie> movieListView;
    @FXML public JFXComboBox<Genre> genreComboBox;
    @FXML public JFXButton sortBtn;
    @FXML public JFXButton clearFiltersBtn;
    @FXML public JFXButton watchlistBtn;

    public List<Movie> allMovies;
    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private SortState sortState = new UnsortedState();

    /**
     * Initialisiert den Controller, richtet die UI-Komponenten ein
     * und meldet sich beim WatchlistRepository als Observer an.
     *
     * @param url            Der Pfad zur FXML-Ressource
     * @param resourceBundle Ressourcen für Internationalisierung
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

        WatchlistRepository.getInstance().addObserver(this);
    }

    /**
     * Lädt Filme aus der Datenbank oder über die API und befüllt die Liste.
     */
    public void initializeState() {
        try {
            MovieRepository repo = MovieRepository.getInstance();
            List<MovieEntity> cachedMovies = repo.getAllMovies();

            if (cachedMovies.isEmpty()) {
                allMovies = MovieAPI.getMovies(null, null, null, null);
                for (Movie movie : allMovies) {
                    MovieEntity entity = new MovieEntity(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getDescription(),
                            movie.getGenres().stream().map(Genre::name).collect(Collectors.joining(",")),
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
            showError(
                    movieListView.getScene().getWindow(),
                    "Initialization Error",
                    "Failed to load movie data",
                    e.getMessage()
            );
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
     * Event-Handler für den "Add to Watchlist"-Button in jeder MovieCell.
     */
    private final ClickEventHandler<Movie> onAddToWatchlistClicked = clickedMovie -> {
        try {
            WatchlistMovieEntity entity = new WatchlistMovieEntity(clickedMovie.getId());
            WatchlistRepository repo = WatchlistRepository.getInstance();
            repo.addToWatchlist(entity);
        } catch (DatabaseException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "Database Error",
                    "Could not add movie to watchlist",
                    e.getMessage()
            );
        }
    };

    /**
     * Wird aufgerufen, wenn das WatchlistRepository eine Benachrichtigung sendet.
     *
     * @param status Der Typ des Ereignisses (ADDED_SUCCESS, ALREADY_EXISTS etc.)
     * @param title  Der Titel des betroffenen Films
     */
    @Override
    public void update(WatchlistStatus status, String title) {
        Window parentWindow = movieListView.getScene().getWindow();
        switch (status) {
            case ALREADY_EXISTS:
                showError(
                        parentWindow,
                        "Watchlist Status Info",
                        "Film bereits in Watchlist",
                        "\"" + title + "\" ist bereits in der Watchlist."
                );
                break;
            case ADDED_SUCCESS:
                showInfo(
                        parentWindow,
                        "Watchlist Status Info",
                        "Film hinzugefügt",
                        "\"" + title + "\" wurde der Watchlist hinzugefügt."
                );
                break;
            default:
                break;
        }
    }

    /**
     * Entfernt alle Filter und lädt den ursprünglichen Filmzustand neu.
     */
    public void clearFilters() {
        genreComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        releaseYearField.clear();
        ratingField.clear();
        initializeState();
    }

    /**
     * Liest alle Filterwerte ein und wendet sie an.
     */
    public void filterMovies() {
        String query = searchField.getText();
        Genre genre = genreComboBox.getValue();
        String releaseYear = releaseYearField.getText();
        String rating = ratingField.getText();
        applyAllFilters(query, genre, releaseYear, rating);
    }

    /**
     * Wendet alle angegebenen Filter auf die Movie-Liste an.
     *
     * @param query       Suchbegriff
     * @param genre       Ausgewähltes Genre (oder null)
     * @param releaseYear Release-Jahr-Filter (oder leer)
     * @param rating      Rating-Filter (oder leer)
     */
    public void applyAllFilters(String query, Genre genre, String releaseYear, String rating) {
        try {
            observableMovies.setAll(
                    MovieAPI.getMovies(
                            query,
                            genre != null ? genre.name() : null,
                            releaseYear,
                            rating
                    )
            );
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
     * Sortiert die Movie-Liste und aktualisiert den Button-Text.
     */
    public void sortMovies() {
        sortState = sortState.next();
        sortState.sort(observableMovies);
        sortBtn.setText(sortState.getButtonLabel());
    }

    /**
     * Setzt die interne Movie-Liste auf die übergebene Liste.
     *
     * @param movies Liste von Movie-Objekten, darf nicht null sein
     * @throws IllegalArgumentException wenn movies null ist
     */
    public void setMovieList(List<Movie> movies) {
        if (movies == null) {
            throw new IllegalArgumentException("Movie list cannot be null");
        }
        this.allMovies = movies;
        observableMovies.setAll(movies);
    }

    /**
     * Wechselt zur Watchlist-Ansicht; meldet davor den aktuellen Controller ab.
     */
    private void switchToWatchlistView() {
        WatchlistRepository.getInstance().removeObserver(this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) watchlistBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(
                    movieListView.getScene().getWindow(),
                    "Load Error",
                    "Could not open watchlist view",
                    e.getMessage()
            );
        }
    }

    /**
     * Ermittelt den populärsten Schauspieler einer Liste von Movies.
     *
     * @param movies Liste von Movie-Objekten
     * @return Name des Schauspielers mit den meisten Auftritten oder null
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
     * Ermittelt die Länge des längsten Filmtitels in einer Liste.
     *
     * @param movies Liste von Movie-Objekten
     * @return Länge des längsten Titels oder 0
     */
    public static int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    /**
     * Zählt, wie viele Filme eines bestimmten Regisseurs in der Liste sind.
     *
     * @param movies   Liste von Movie-Objekten
     * @param director Name des Regisseurs
     * @return Anzahl der Filme dieses Regisseurs
     */
    public static long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    /**
     * Liefert alle Filme, die zwischen zwei Jahren veröffentlicht wurden.
     *
     * @param movies    Liste von Movie-Objekten
     * @param startYear Startjahr (inklusive)
     * @param endYear   Endjahr (inklusive)
     * @return Liste der Filme in diesem Zeitraum
     */
    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}
