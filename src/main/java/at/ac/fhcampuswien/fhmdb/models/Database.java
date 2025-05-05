package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:h2:./database";
    private static final String username = "sa";
    private static final String password = "";

    private ConnectionSource conn;
    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public void createConnectionsSource() throws SQLException {
        conn = new JdbcConnectionSource(DB_URL, username, password);
    }

    public ConnectionSource getConnectionSource() {
        return conn;
    }

    public void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(conn, MovieEntity.class);
        TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    // Initialize DAOs after connection is established
    public void initializeDaos() throws SQLException {
        movieDao = com.j256.ormlite.dao.DaoManager.createDao(conn, MovieEntity.class);
        watchlistDao = com.j256.ormlite.dao.DaoManager.createDao(conn, WatchlistMovieEntity.class);
    }
} 