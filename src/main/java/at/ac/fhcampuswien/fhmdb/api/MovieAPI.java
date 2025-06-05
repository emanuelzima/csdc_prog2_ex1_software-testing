package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final String USER_AGENT = "http.agent";

    // Ruft die API mit optionalen Parametern auf und liefert eine Liste von Movies zurück
    public static List<Movie> getMovies(String query, String genre, String releaseYear, String ratingFrom) throws MovieApiException {
        // 1) URL mit Builder zusammenstellen
        String finalUrl = new MovieApiRequestBuilder(BASE_URL)
                .query(query)
                .genre(genre)
                .releaseYear(releaseYear)
                .ratingFrom(ratingFrom)
                .build();

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
            List<Movie> movies = new Gson().fromJson(jsonBody, new TypeToken<List<Movie>>() {
            }.getType());
            if (movies == null) {
                return new ArrayList<>();
            }
            return movies;
        } catch (IOException e) {
            throw new MovieApiException(e);
        }
    }
}
