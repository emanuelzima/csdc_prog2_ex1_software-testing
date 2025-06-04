package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing movie data in the database.
 * This class encapsulates all database operations for movies and provides methods
 * for adding, retrieving, and managing movies.
 */
public class MovieRepository {
    private final Database database;

    /**
     * Constructor initializes the repository with the provided database.
     */
    public MovieRepository() {
        this.database = Database.getInstance();
    }

    /**
     * Adds a movie to the database.
     * @param movie The movie to add
     * @throws DatabaseException if database access fails
     */
    public void addMovie(MovieEntity movie) throws DatabaseException {
        try {
            database.save(movie);
        } catch (Exception e) {
            throw new DatabaseException("Failed to add movie to database", e);
        }
    }

    /**
     * Removes a movie from the database.
     * @param movie The movie to remove
     * @throws DatabaseException if database access fails
     */
    public void removeMovie(MovieEntity movie) throws DatabaseException {
        try {
            database.delete(movie);
        } catch (Exception e) {
            throw new DatabaseException("Failed to remove movie from database", e);
        }
    }

    /**
     * Retrieves all movies from the database.
     * @return List of all movies
     * @throws DatabaseException if database access fails
     */
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return database.findAll(MovieEntity.class);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all movies from database", e);
        }
    }

    /**
     * Retrieves a movie by its ID.
     * @param id The ID of the movie to retrieve
     * @return Optional containing the found movie, or empty if not found
     * @throws DatabaseException if database access fails
     */
    public Optional<MovieEntity> getMovieById(Long id) throws DatabaseException {
        try {
            return database.findById(MovieEntity.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get movie by ID from database", e);
        }
    }

    /**
     * Retrieves movies by their API ID.
     * @param apiId The API ID of the movies to retrieve
     * @return List of found movies
     * @throws DatabaseException if database access fails
     */
    public List<MovieEntity> getMoviesByApiId(String apiId) throws DatabaseException {
        try {
            return database.findByField(MovieEntity.class, "apiId", apiId);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get movie by API ID from database", e);
        }
    }
} 