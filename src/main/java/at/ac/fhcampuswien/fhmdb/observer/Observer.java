package at.ac.fhcampuswien.fhmdb.observer;

import at.ac.fhcampuswien.fhmdb.models.WatchlistStatus;

/**
 * Interface für Klassen, die auf Watchlist-Änderungen reagieren möchten.
 */
public interface Observer {
    /**
     * Wird aufgerufen, sobald das Observable eine Benachrichtigung sendet.
     *
     * @param status Der Typ des Ereignisses (z. B. ADDED_SUCCESS)
     * @param title  Der Titel des betroffenen Films
     */
    void update(WatchlistStatus status, String title);
}
