package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing watchlist data in the database.
 * This class encapsulates all database operations for the watchlist and provides methods
 * for adding, removing, and retrieving movies from the watchlist.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
public class WatchlistRepository {
    private static WatchlistRepository instance;
    private final Database database;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private WatchlistRepository() {
        this.database = Database.getInstance();
    }

    /**
     * Gets the singleton instance of WatchlistRepository.
     * @return The singleton instance
     */
    public static synchronized WatchlistRepository getInstance() {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    /**
     * Adds a movie to the watchlist if it's not already present.
     * @param movie The movie to add to the watchlist
     * @throws DatabaseException if database access fails
     */
    public void addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            // Check if movie is already in watchlist
            List<WatchlistMovieEntity> existingMovies = getWatchlistMoviesByApiId(movie.getApiId());
            if (!existingMovies.isEmpty()) {
                throw new DatabaseException("Movie is already in your watchlist", null);
            }
            database.save(movie);
        } catch (Exception e) {
            throw new DatabaseException("Failed to add movie to watchlist", e);
        }
    }

    /**
     * Removes a movie from the watchlist by its API ID.
     * @param movie The movie to remove from the watchlist
     * @throws DatabaseException if database access fails
     */
    public void removeFromWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            database.delete(movie);
        } catch (Exception e) {
            throw new DatabaseException("Failed to remove movie from watchlist", e);
        }
    }

    /**
     * Retrieves all movies from the watchlist.
     * @return List of all watchlist entries
     * @throws DatabaseException if database access fails
     */
    public List<WatchlistMovieEntity> getAllWatchlistMovies() throws DatabaseException {
        try {
            return database.findAll(WatchlistMovieEntity.class);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all watchlist movies", e);
        }
    }

    /**
     * Retrieves a movie from the watchlist by its ID.
     * @param id The ID of the movie to retrieve
     * @return Optional containing the movie if found, empty if not found
     * @throws DatabaseException if database access fails
     */
    public Optional<WatchlistMovieEntity> getWatchlistMovieById(Long id) throws DatabaseException {
        try {
            return database.findById(WatchlistMovieEntity.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get watchlist movie by ID", e);
        }
    }

    /**
     * Retrieves all movies from the watchlist by their API ID.
     * @param apiId The API ID of the movies to retrieve
     * @return List of movies with the specified API ID
     * @throws DatabaseException if database access fails
     */
    public List<WatchlistMovieEntity> getWatchlistMoviesByApiId(String apiId) throws DatabaseException {
        try {
            return database.findByField(WatchlistMovieEntity.class, "apiId", apiId);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get watchlist movie by API ID", e);
        }
    }
} 