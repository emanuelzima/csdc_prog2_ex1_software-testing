package at.ac.fhcampuswien.fhmdb.api;

import okhttp3.HttpUrl;

import java.util.Objects;

public class MovieApiRequestBuilder {
    private final HttpUrl.Builder urlBuilder;

    public MovieApiRequestBuilder(String baseUrl) {
        // Startet mit der Basis-URL
        this.urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
    }

    // Setter für jeden optionalen Parameter
    public MovieApiRequestBuilder query(String query) {
        if (query != null && !query.isBlank()) {
            this.urlBuilder.addQueryParameter("query", query);
        }
        return this;
    }

    public MovieApiRequestBuilder genre(String genre) {
        if (genre != null && !genre.isBlank()) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        return this;
    }

    public MovieApiRequestBuilder releaseYear(String releaseYear) {
        if (releaseYear != null && !releaseYear.isBlank()) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        return this;
    }

    public MovieApiRequestBuilder ratingFrom(String rating) {
        if (rating != null && !rating.isBlank()) {
            urlBuilder.addQueryParameter("ratingFrom", rating);
        }
        return this;
    }

    // builder
    public String build() {
        return urlBuilder.build().toString();
    }
}


