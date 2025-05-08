package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return dao.queryForAll();
    }

    public int addToWatchlist(WatchlistMovieEntity movie) throws SQLException {
        // Only add if not already present
        List<WatchlistMovieEntity> existing = dao.queryForEq("apiId", movie.getApiId());
        if (existing.isEmpty()) {
            return dao.create(movie);
        }
        return 0;
    }

    public int removeFromWatchlist(String apiId) throws SQLException {
        List<WatchlistMovieEntity> entries = dao.queryForEq("apiId", apiId);
        int count = 0;
        for (WatchlistMovieEntity entry : entries) {
            count += dao.delete(entry);
        }
        return count;
    }

    public List<String> getAllWatchlistApiIds() throws SQLException {
        List<WatchlistMovieEntity> entries = dao.queryForAll();
        return entries.stream()
                .map(WatchlistMovieEntity::getApiId)
                .collect(Collectors.toList());
    }
} 