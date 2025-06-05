package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface SortState {
    void sort(List<Movie> movies);

    // gibt den nächsten Zustand
    SortState next();
    String getButtonLabel();
}
