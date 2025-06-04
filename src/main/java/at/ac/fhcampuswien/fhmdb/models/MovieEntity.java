package at.ac.fhcampuswien.fhmdb.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a movie in the database.
 * This class is annotated with ORMLite annotations and serves as a database entity
 * for the "movies" table. It contains all the necessary fields to store movie information
 * and provides methods to convert between Movie and MovieEntity objects.
 */
@Entity
@Table(name = "movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String apiId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column
    private String genres;

    @Column
    private int releaseYear;

    @Column
    private String imgUrl;

    @Column
    private int lengthInMinutes;

    @Column
    private double rating;

    // Default constructor required by Hibernate
    public MovieEntity() {
    }

    public MovieEntity(String apiId, String title, String description, String genres, 
                      int releaseYear, String imgUrl, int lengthInMinutes, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    /**
     * Converts a list of genres to a comma-separated string.
     * @param genres List of genres to convert
     * @return Comma-separated string of genre names
     */
    public static String genresToString(List<Genre> genres) {
        return genres.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    /**
     * Converts a list of Movie objects to MovieEntity objects.
     * @param movies List of Movie objects to convert
     * @return List of converted MovieEntity objects
     */
    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream().map(movie -> {
            MovieEntity entity = new MovieEntity();
            entity.apiId = movie.getId();
            entity.title = movie.getTitle();
            entity.description = movie.getDescription();
            entity.genres = genresToString(movie.getGenres());
            entity.releaseYear = movie.getReleaseYear();
            entity.imgUrl = movie.getImgUrl();
            entity.lengthInMinutes = movie.getLengthInMinutes();
            entity.rating = movie.getRating();
            return entity;
        }).collect(Collectors.toList());
    }

    /**
     * Converts a list of MovieEntity objects to Movie objects.
     * @param movieEntities List of MovieEntity objects to convert
     * @return List of converted Movie objects
     */
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        return movieEntities.stream().map(entity -> {
            List<Genre> genreList = Arrays.stream(entity.genres.split(","))
                    .map(String::trim)
                    .map(Genre::valueOf)
                    .collect(Collectors.toList());

            return new Movie(
                    entity.apiId,
                    entity.title,
                    entity.description,
                    genreList,
                    entity.releaseYear,
                    entity.imgUrl,
                    entity.lengthInMinutes,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    entity.rating
            );
        }).collect(Collectors.toList());
    }

    // Getters and Setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
} 