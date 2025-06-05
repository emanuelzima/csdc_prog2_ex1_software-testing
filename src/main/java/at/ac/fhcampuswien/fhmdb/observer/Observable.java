package at.ac.fhcampuswien.fhmdb.observer;

import at.ac.fhcampuswien.fhmdb.models.WatchlistStatus;

/**
 * Interface für Klassen, die Observer verwalten und bei Änderungen benachrichtigen.
 */
public interface Observable {
    /**
     * Registriert einen Observer.
     *
     * @param o Der Observer, der sich anmelden möchte
     */
    void addObserver(Observer o);

    /**
     * Entfernt einen Observer.
     *
     * @param o Der Observer, der abgemeldet werden soll
     */
    void removeObserver(Observer o);

    /**
     * Informiert alle registrierten Observer über eine Änderung.
     *
     * @param status Der Typ des Ereignisses (z. B. ADDED_SUCCESS)
     * @param title  Der Titel des betroffenen Films
     */
    void notifyObservers(WatchlistStatus status, String title);
}
