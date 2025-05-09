package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private static final String DB_URL = "jdbc:h2:./database";
    private static final String username = "sa";
    private static final String password = "";

    private ConnectionSource conn;
    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    private Database() {} // privater Konstruktor

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void initialize() throws DatabaseException {
        if (conn == null) {
            createConnectionsSource();
            createTables();
            initializeDaos();
        }
    }

    public void createConnectionsSource() throws DatabaseException {
        try
        {
            conn = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e) {
            throw new DatabaseException("Es wurde kein Treiber für die zu erstellende Datenbank gefunden.",e);
        }
    }

    public void createTables() throws DatabaseException {
        try
        {
            TableUtils.createTableIfNotExists(conn, MovieEntity.class);
            TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
        }catch (SQLException e)
        {
            throw new DatabaseException("Es ist ein Problem bei dem Erstellen der Tabellen der Datenbank aufgetreten.", e);
        }
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    // Initialize DAOs after connection is established
    public void initializeDaos() throws DatabaseException {
        try
        {
            movieDao = com.j256.ormlite.dao.DaoManager.createDao(conn, MovieEntity.class);
            watchlistDao = com.j256.ormlite.dao.DaoManager.createDao(conn, WatchlistMovieEntity.class);
        }catch (SQLException e)
        {
            throw new DatabaseException("Es ist ein Fehler bei dem Speichern passiert.", e);
        }
    }
} 