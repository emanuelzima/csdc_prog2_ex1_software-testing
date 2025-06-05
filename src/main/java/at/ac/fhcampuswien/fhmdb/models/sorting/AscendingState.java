package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;
import java.util.List;

public class AscendingState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        movies.sort(Comparator.comparing(Movie::getTitle));
    }

    @Override
    public SortState next() {
        return new DescendingState();
    }

    @Override
    public String getButtonLabel() {
        return "Sort (desc)";
    }
}
