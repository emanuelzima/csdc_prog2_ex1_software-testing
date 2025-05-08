package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieRepository {
    private final Dao<MovieEntity, Long> dao;

    public MovieRepository(Dao<MovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public int removeAll() throws SQLException {
        return dao.deleteBuilder().delete();
    }

    public MovieEntity getMovie(Long id) throws SQLException {
        return dao.queryForId(id);
    }

    public int addAllMovies(List<Movie> movies) throws SQLException {
        int count = 0;
        for (Movie movie : movies) {
            List<MovieEntity> existing = dao.queryForEq("apiId", movie.getId());
            if (existing.isEmpty()) {
                MovieEntity entity = new MovieEntity();
                entity.setApiId(movie.getId());
                entity.setTitle(movie.getTitle());
                entity.setDescription(movie.getDescription());
                entity.setGenres(MovieEntity.genresToString(movie.getGenres()));
                entity.setReleaseYear(movie.getReleaseYear());
                entity.setImgUrl(movie.getImgUrl());
                entity.setLengthInMinutes(movie.getLengthInMinutes());
                entity.setRating(movie.getRating());

                dao.create(entity);
                count++;
            }
        }
        return count;
    }

    public List<MovieEntity> getMoviesByApiIds(List<String> apiIds) throws SQLException {
        List<MovieEntity> result = new ArrayList<>();
        for (String apiId : apiIds) {
            result.addAll(dao.queryForEq("apiId", apiId));
        }
        return result;
    }

    public void removeDuplicateMovies() throws SQLException {
        List<MovieEntity> all = dao.queryForAll();
        Set<String> seenApiIds = new HashSet<>();
        for (MovieEntity movie : all) {
            String apiId = movie.getApiId();

            if (apiId == null || apiId.isBlank()) {
                System.out.println("Entferne Film ohne gültige apiId: " + movie.getTitle());
                dao.delete(movie);
                continue;
            }

            if (!seenApiIds.add(apiId)) {
                System.out.println("Entferne Duplikat: " + movie.getTitle());
                dao.delete(movie);
            }
        }
    }
} 