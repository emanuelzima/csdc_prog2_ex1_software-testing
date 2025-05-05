package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Long> dao;

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
            MovieEntity entity = new MovieEntity();
            // Map fields from Movie to MovieEntity as needed
            // entity.setTitle(movie.getTitle()); ...
            dao.create(entity);
            count++;
        }
        return count;
    }
} 