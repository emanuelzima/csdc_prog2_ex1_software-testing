package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class UnsortedState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        // originale reihenfolge wie es die API liefert
    }

    @Override
    public SortState next() {
        return new AscendingState();
    }

    @Override
    public String getButtonLabel() {
        return "Sort (asc)";
    }
}
