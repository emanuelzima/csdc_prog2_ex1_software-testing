package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final String USER_AGENT = "http.agent";

    // Ruft die API mit optionalen Parametern auf und liefert eine Liste von Movies zurück
    public static List<Movie> getMovies(String query, String genre, String releaseYear, String ratingFrom) throws IOException {
        // 1) URL dynamisch aufbauen
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder();

        if (query != null && !query.isBlank()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isBlank()) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null && !releaseYear.isBlank()) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (ratingFrom != null && !ratingFrom.isBlank()) {
            urlBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }

        // URL bauen
        String finalUrl = urlBuilder.build().toString();

        // 2) Request vorbereiten
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl)
                // User-Agent Header setzen (Pflicht laut Aufgabenstellung)
                .header("User-Agent", USER_AGENT)
                .build();

        // 3) Request ausführen
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Fehlermeldung, wenn HTTP-Code nicht 200..299
                throw new IOException("Unexpected code " + response);
            }

            // 4) JSON-Antwort in String umwandeln
            assert response.body() != null;
            String jsonBody = response.body().string();

            // 5) JSON-String per Gson in eine Liste von Movies parsen
            List<Movie> movies = new Gson().fromJson(jsonBody, new TypeToken<List<Movie>>(){}.getType());
            if (movies == null) {
                return new ArrayList<>();
            }
            return movies;
        }
    }
}
