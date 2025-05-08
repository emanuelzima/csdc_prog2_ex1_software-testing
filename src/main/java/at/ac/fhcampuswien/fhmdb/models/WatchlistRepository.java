package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;

import java.sql.DataTruncation;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try
        {
            return dao.queryForAll();
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
    }

    public int addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        // Only add if not already present
        try
        {
            List<WatchlistMovieEntity> existing = dao.queryForEq("apiId", movie.getApiId());
            if (existing.isEmpty()) {
                return dao.create(movie);
            }
            return 0;
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try
        {
            List<WatchlistMovieEntity> entries = dao.queryForEq("apiId", apiId);
            int count = 0;
            for (WatchlistMovieEntity entry : entries) {
                count += dao.delete(entry);
            }
            return count;
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
    }

    public List<String> getAllWatchlistApiIds() throws DatabaseException {
        try
        {
            List<WatchlistMovieEntity> entries = dao.queryForAll();
            return entries.stream()
                    .map(WatchlistMovieEntity::getApiId)
                    .collect(Collectors.toList());
        }catch (SQLException e)
        {
            throw new DatabaseException(e);
        }
    }
} 