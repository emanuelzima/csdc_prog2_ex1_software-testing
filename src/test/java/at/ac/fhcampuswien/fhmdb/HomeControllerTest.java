package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;
    private List<Movie> testMovies;

    @BeforeEach
    public void cleanStart() {
        homeController = new HomeController();
        // Create test movies
        testMovies = Arrays.asList(
            new Movie("1", "Inception", "A thief who steals corporate secrets", 
                List.of(Genre.ACTION), 2010, "url1", 148, 
                List.of("Christopher Nolan"), List.of("Christopher Nolan"), 
                List.of("Leonardo DiCaprio"), 8.8),
            new Movie("2", "The Dark Knight", "When the menace known as the Joker", 
                List.of(Genre.ACTION, Genre.DRAMA), 2008, "url2", 152,
                List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Christian Bale"), 9.0),
            new Movie("3", "Pulp Fiction", "The lives of two mob hitmen", 
                List.of(Genre.CRIME, Genre.DRAMA), 1994, "url3", 154,
                List.of("Quentin Tarantino"), List.of("Quentin Tarantino"),
                List.of("John Travolta"), 8.9)
        );
        homeController.setMovieList(testMovies);
    }

    @Test
    void afterInitialization_allMoviesAndObservableMovies_shouldBeTheSame() {
        assertEquals(homeController.allMovies, homeController.observableMovies);
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

    /**
     * Tests for Exercise 2
     */
    @Test
    void getMostPopularActor_returnsMostPopularActor() {
        // given:
        String mostPopularActor = HomeController.getMostPopularActor(testMovies);
        // then:
        assertTrue(List.of("Leonardo DiCaprio", "Christian Bale", "John Travolta")
            .contains(mostPopularActor));
    }

    @Test
    void getLongestMovieTitle_returnsLongestMovieTitle() {
        // given:
        int expected = "The Dark Knight".length();
        // when:
        int actual = HomeController.getLongestMovieTitle(testMovies);
        // then:
        assertEquals(expected, actual);
    }

    @Test
    void countMoviesFrom_returnsNumberOfMovies() {
        // given:
        String director = "Christopher Nolan";
        long expected = testMovies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
        // when:
        long actual = HomeController.countMoviesFrom(testMovies, director);
        // then:
        assertEquals(expected, actual);
    }

    @Test
    void getMoviesBetweenYears_returnsMoviesBetweenYears() {
        // given:
        int start = 2000;
        int end = 2019;
        List<Movie> expected = testMovies.stream()
                .filter(m -> m.getReleaseYear() >= start && m.getReleaseYear() <= end)
                .toList();
        // when:
        List<Movie> actual = HomeController.getMoviesBetweenYears(testMovies, start, end);
        // then:
        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
    }
}
