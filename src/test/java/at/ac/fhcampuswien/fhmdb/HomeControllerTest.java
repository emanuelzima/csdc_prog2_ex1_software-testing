package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    public void cleanStart() {
        homeController = new HomeController();
        homeController.initializeState();
    }

    @Test
    void afterInitialization_allMoviesAndObservableMovies_shouldBeTheSame() {
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    /**
     * Testing filter methods
     */
    @Test
    void filterByQuery_withMatchingCaseInsensitiveQueryInTitle_returnsExpectedMovies() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> filtered = homeController.filterByQuery(movies, "inCEPTioN");
        // then:
        for (Movie movie : filtered) {
            String title = movie.getTitle().toLowerCase();
            assertTrue(title.contains("inception"));
        }
    }

    @Test
    void filterByQuery_withMatchingCaseInsensitiveQueryInDescription_returnsExpectedMovies() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> filtered = homeController.filterByQuery(movies, "EXploR");
        // then:
        for (Movie movie : filtered) {
            String description = movie.getDescription().toLowerCase();
            assertTrue(description.contains("explor"));
        }
    }

    @Test
    void filterByQuery_withNonMatchingQuery_returnsEmptyList() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> filtered = homeController.filterByQuery(movies, "TeSThALlo");
        // then:
        assertTrue(filtered.isEmpty());
    }

    @Test
    void filterByQuery_withNullQuery_returnsOriginalList() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> resultNull = homeController.filterByQuery(movies, null);
        // then:
        assertEquals(movies, resultNull);
    }

    @Test
    void filterByQuery_withEmptyQuery_returnsOriginalList() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> resultEmpty = homeController.filterByQuery(movies, "");
        // then:
        assertEquals(movies, resultEmpty);
    }

    @Test
    void filterByQuery_withWhitespaceAsQuery_returnsOriginalList() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> resultEmpty = homeController.filterByQuery(movies, "   ");
        // then:
        assertEquals(movies, resultEmpty);
    }

    @Test
    void filterByGenre_withNullGenre_returnsOriginalList() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        // when:
        List<Movie> filtered = homeController.filterByGenre(movies, null);
        // then:
        assertEquals(movies, filtered);
    }

    @Test
    void filterByGenre_returnsOnlyMoviesWithGivenGenre() {
        // given:
        List<Movie> movies = Movie.initializeMovies();
        Genre targetGenre = Genre.DRAMA;
        // when
        List<Movie> filtered = homeController.filterByGenre(movies, targetGenre);
        // then:
        for (Movie movie : filtered) {
            assertTrue(movie.getGenres().contains(targetGenre));
        }
    }

    @Test
    void applyAllFilters_withEmptyQueryFields_returnsAllMovies() {
        // given:
        homeController.setMovieList(Movie.initializeMovies());
        // when:
        homeController.applyAllFilters("", null, "", "");
        // then:
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void applyAllFilters_WhenQueryMatches_AddsMoviesToObservableMovies() {
        // given:
        Movie movie1 = new Movie("Action Movie", "Exciting action", List.of(Genre.ACTION));
        Movie movie2 = new Movie("Comedy Movie", "Hilarious comedy", List.of(Genre.COMEDY));
        homeController.setMovieList(List.of(movie1, movie2));
        // when:
        homeController.applyAllFilters("action movie", null, "", "");
        // then:
        assertTrue(homeController.observableMovies.contains(movie1));
    }

    @Test
    void applyAllFilters_WhenQueryDoesNotMatch_ExcludesMoviesFromObservableMovies() {
        // given:
        Movie movie1 = new Movie("Action Movie", "Exciting action", List.of(Genre.ACTION));
        Movie movie2 = new Movie("Comedy Movie", "Hilarious comedy", List.of(Genre.COMEDY));
        homeController.setMovieList(List.of(movie1, movie2));
        // when:
        homeController.applyAllFilters("action movie", null, "", "");
        // then:
        assertFalse(homeController.observableMovies.contains(movie2));
    }

    @Test
    void applyAllFilters_WhenGenreMatches_AddsMoviesToObservableMovies() {
        // given:
        Movie movie1 = new Movie("Action Movie", "Exciting action", List.of(Genre.ACTION));
        Movie movie2 = new Movie("Comedy Movie", "Hilarious comedy", List.of(Genre.COMEDY));
        homeController.setMovieList(List.of(movie1, movie2));
        // when:
        homeController.applyAllFilters("", Genre.ACTION, "", "");
        // then:
        assertTrue(homeController.observableMovies.contains(movie1));
    }

    @Test
    void applyAllFilters_WhenGenreDoesNotMatch_ExcludesMoviesFromObservableMovies() {
        // given:
        Movie movie1 = new Movie("Action Movie", "Exciting action", List.of(Genre.ACTION));
        Movie movie2 = new Movie("Comedy Movie", "Hilarious comedy", List.of(Genre.COMEDY));
        homeController.setMovieList(List.of(movie1, movie2));
        // when:
        homeController.applyAllFilters("", Genre.ACTION, "", "");
        // then:
        assertFalse(homeController.observableMovies.contains(movie2));
    }

    /**
     * Testing sortMovies
     */
    @Test
    void sortMovies_forTheFirstTime_togglesSortingOrderToAscending() {
        // given:
        homeController.sortedState = SortedState.NONE;
        // when:
        homeController.sortMovies();
        // then:
        assertEquals(SortedState.ASCENDING, homeController.sortedState);
    }

    @Test
    void sortMovies_forTheFirstTime_producesAscendingOrder() {
        // given:
        homeController.sortedState = SortedState.NONE;
        // when:
        homeController.sortMovies();
        // then:
        List<Movie> ascSorted = homeController.observableMovies;
        for (int i = 1; i < ascSorted.size(); i++) {
            assertTrue(ascSorted.get(i - 1).getTitle().compareTo(ascSorted.get(i).getTitle()) <= 0);
        }
    }

    @Test
    void sortMovies_WhenOrderIsDescending_togglesSortingOrderToAscending() {
        // given:
        homeController.sortedState = SortedState.DESCENDING;
        // when:
        homeController.sortMovies();
        // then:
        assertEquals(SortedState.ASCENDING, homeController.sortedState);
    }

    @Test
    void sortMovies_WhenOrderIsDescending_producesAscendingOrder() {
        // given:
        homeController.sortedState = SortedState.DESCENDING;
        // when:
        homeController.sortMovies();
        // then:
        List<Movie> ascSorted = homeController.observableMovies;
        for (int i = 1; i < ascSorted.size(); i++) {
            assertTrue(ascSorted.get(i - 1).getTitle().compareTo(ascSorted.get(i).getTitle()) <= 0);
        }
    }

    @Test
    void sortMovies_WhenOrderIsAscending_togglesSortingOrderToDescending() {
        // given:
        homeController.sortedState = SortedState.ASCENDING;
        // when:
        homeController.sortMovies();
        // then:
        assertEquals(SortedState.DESCENDING, homeController.sortedState);
    }

    @Test
    void sortMovies_WhenOrderIsAscending_producesDescendingOrder() {
        // given:
        homeController.sortedState = SortedState.ASCENDING;
        // when:
        homeController.sortMovies();
        // then:
        List<Movie> descSorted = homeController.observableMovies;
        for (int i = 1; i < descSorted.size(); i++) {
            assertTrue(descSorted.get(i - 1).getTitle().compareTo(descSorted.get(i).getTitle()) >= 0);
        }
    }
}
