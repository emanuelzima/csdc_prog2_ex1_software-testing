package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.List;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String genres;

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private int lengthInMinutes;

    @DatabaseField
    private double rating;

    // Method signatures from the diagram
    public static String genresToString(List<Genre> genres) { return null; }
    public static List<MovieEntity> fromMovies(List<Movie> movies) { return null; }
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) { return null; }

    // Getters and setters omitted for brevity
} 