package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static String genresToString(List<Genre> genres) {
        return genres.stream().map(Enum::name).collect(Collectors.joining(","));
    }

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

    public String getApiId() {return apiId;}

    public void setApiId(String apiId) {this.apiId = apiId;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getGenres() {return genres;}

    public void setGenres(String genres) {this.genres = genres;}

    public int getReleaseYear() {return releaseYear;}

    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}

    public String getImgUrl() {return imgUrl;}

    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}

    public int getLengthInMinutes() {return lengthInMinutes;}

    public void setLengthInMinutes(int lengthInMinutes) {this.lengthInMinutes = lengthInMinutes;}

    public double getRating() {return rating;}

    public void setRating(double rating) {this.rating = rating;}
} 