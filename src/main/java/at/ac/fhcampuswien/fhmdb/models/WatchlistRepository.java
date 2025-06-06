package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository-Klasse für Watchlist-Daten.
 * Implementiert das Observable-Interface, um Observer über Änderungen zu informieren.
 */
public class WatchlistRepository implements Observable {
    private static volatile WatchlistRepository instance;
    private final Database database;
    private final List<Observer> observers = new ArrayList<>();

    private WatchlistRepository() {
        this.database = Database.getInstance();
    }

    /**
     * Gibt die Singleton-Instanz zurück.
     *
     * @return Die einzige Instanz von WatchlistRepository
     */
    public static WatchlistRepository getInstance() {
        WatchlistRepository result = instance;
        if (result == null) {
            synchronized (WatchlistRepository.class) {
                result = instance;
                if (result == null) {
                    result = new WatchlistRepository();
                    instance = result;
                }
            }
        }
        return result;
    }

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(WatchlistStatus status, String title) {
        for (Observer o : observers) {
            o.update(status, title);
        }
    }

    /**
     * Fügt einen Film in die Watchlist ein. Informiert Observer:
     * - ALREADY_EXISTS, falls der Film schon vorhanden ist
     * - ADDED_SUCCESS, falls das Hinzufügen erfolgreich war
     *
     * @param movieEntity Die WatchlistMovieEntity, die eingefügt werden soll
     * @throws DatabaseException Wenn ein Datenbankfehler auftritt
     */
    public void addToWatchlist(WatchlistMovieEntity movieEntity) throws DatabaseException {
        try {
            List<WatchlistMovieEntity> existingMovies = getWatchlistMoviesByApiId(movieEntity.getApiId());
            String title = lookupTitle(movieEntity.getApiId());

            if (!existingMovies.isEmpty()) {
                notifyObservers(WatchlistStatus.ALREADY_EXISTS, title);
                return;
            }

            database.save(movieEntity);
            notifyObservers(WatchlistStatus.ADDED_SUCCESS, title);

        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to add movie to watchlist", e);
        }
    }

    /**
     * Entfernt einen Film aus der Watchlist. Informiert Observer:
     * - NOT_FOUND, falls der Film nicht in der Watchlist aber trotzdem im UI war
     * - REMOVED_SUCCESS, falls das Entfernen erfolgreich war
     *
     * @param movieEntity Die WatchlistMovieEntity, die entfernt werden soll
     * @throws DatabaseException Wenn ein Datenbankfehler auftritt
     */
    public void removeFromWatchlist(WatchlistMovieEntity movieEntity) throws DatabaseException {
        try {
            Optional<WatchlistMovieEntity> potentialMovies = getWatchlistMovieById(movieEntity.getId());
            String title = lookupTitle(movieEntity.getApiId());

            if (potentialMovies.isEmpty()) {
                notifyObservers(WatchlistStatus.NOT_FOUND, title);
                return;
            }

            database.delete(movieEntity);
            notifyObservers(WatchlistStatus.REMOVED_SUCCESS, title);

        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to remove movie from watchlist", e);
        }
    }

    /**
     * Ruft alle Watchlist-Einträge ab.
     *
     * @return Liste aller WatchlistMovieEntity-Einträge
     * @throws DatabaseException Wenn ein Fehler beim Datenbankzugriff auftritt
     */
    public List<WatchlistMovieEntity> getAllWatchlistMovies() throws DatabaseException {
        try {
            return database.findAll(WatchlistMovieEntity.class);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all watchlist movies", e);
        }
    }

    /**
     * Sucht einen Watchlist-Eintrag anhand seiner Datenbank-ID.
     *
     * @param id Die Primärschlüssel-ID des WatchlistMovieEntity
     * @return Optional mit dem Eintrag, wenn gefunden
     * @throws DatabaseException Wenn ein Fehler beim Datenbankzugriff auftritt
     */
    public Optional<WatchlistMovieEntity> getWatchlistMovieById(Long id) throws DatabaseException {
        try {
            return database.findById(WatchlistMovieEntity.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get watchlist movie by ID", e);
        }
    }

    /**
     * Sucht alle Watchlist-Einträge mit der gegebenen API-ID.
     *
     * @param apiId Die externe API-ID des Films
     * @return Liste aller Einträge mit dieser API-ID
     * @throws DatabaseException Wenn ein Fehler beim Datenbankzugriff auftritt
     */
    public List<WatchlistMovieEntity> getWatchlistMoviesByApiId(String apiId) throws DatabaseException {
        try {
            return database.findByField(WatchlistMovieEntity.class, "apiId", apiId);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get watchlist movie by API ID", e);
        }
    }

    /**
     * Liest den Filmtitel anhand der API-ID aus MovieRepository aus.
     * Fallback: Gibt die API-ID zurück, falls kein Titel gefunden oder ein Fehler auftritt.
     *
     * @param apiId Die API-ID des Films
     * @return Den Filmtitel oder die API-ID als Fallback
     */
    private String lookupTitle(String apiId) {
        try {
            MovieRepository movieRepo = MovieRepository.getInstance();
            List<MovieEntity> entities = movieRepo.getMoviesByApiId(apiId);
            if (!entities.isEmpty()) {
                return entities.get(0).getTitle();
            }
        } catch (DatabaseException e) {
            // Fallback: apiId zurückgeben
        }
        return apiId;
    }
}
