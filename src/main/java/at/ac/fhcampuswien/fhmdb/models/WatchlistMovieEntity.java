package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    // Getters and setters omitted for brevity

    public String getApiId() {
        return apiId;
    }
} 