package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
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

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try
        {
            return dao.queryForAll();
        }catch (SQLException e)
        {
            throw new DatabaseException("Folgender Fehler trat bei Zugriff auf die Datenbank auf " + e.getMessage(), e);
        }
    }

    public int removeAll() throws DatabaseException {
        try
        {
            return dao.deleteBuilder().delete();
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }

    }

    public MovieEntity getMovie(Long id) throws DatabaseException {
        try
        {
            return dao.queryForId(id);
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }

    }

    public int addAllMovies(List<Movie> movies) throws DatabaseException {
        int count = 0;
        try
        {
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
        }catch (SQLException e)
        {
            throw new DatabaseException("API nicht erreichbar - Filme werden aus DB geladen.", e);
        }
        return count;
    }

    public List<MovieEntity> getMoviesByApiIds(List<String> apiIds) throws DatabaseException {
        List<MovieEntity> result = new ArrayList<>();
        try
        {
            for (String apiId : apiIds) {
                result.addAll(dao.queryForEq("apiId", apiId));
            }
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
        return result;
    }

    public void removeDuplicateMovies() throws DatabaseException {
        try
        {
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
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
    }
} 