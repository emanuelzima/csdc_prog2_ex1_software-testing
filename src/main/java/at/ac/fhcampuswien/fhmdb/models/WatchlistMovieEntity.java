package at.ac.fhcampuswien.fhmdb.models;

import jakarta.persistence.*;

/**
 * Represents a movie in the watchlist database.
 * This class is annotated with Hibernate annotations and serves as a database entity
 * for the "watchlist" table. It contains only the necessary fields for watchlist functionality.
 * The complete movie data is referenced through the apiId from the movies table.
 */
@Entity
@Table(name = "watchlist")
public class WatchlistMovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String apiId;

    // Default constructor required by Hibernate
    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}

