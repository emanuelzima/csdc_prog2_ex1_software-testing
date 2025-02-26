package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) throws IllegalArgumentException {
        if(isCreationOk(title, description, genres))
        {
            this.title = title;
            this.description = description;
            this.genres = genres;
        }
    }

    private boolean isCreationOk(String title, String description, List<Genre> genres) throws IllegalArgumentException
    {
        String errorMessage = "";
        boolean errorExists = false;

        if(title.isBlank())
        {
            errorMessage = "The movie has to have a title.\n";
            errorExists = true;
        }
        if(description.isBlank())
        {
            errorMessage = errorMessage + "The movie has to have a description.\n";
            errorExists = true;
        }
        if(genres.isEmpty())
        {
            errorMessage = errorMessage + "The movie has to have genres.";
            errorExists = true;
        }

        if(errorExists)
        {
            throw new IllegalArgumentException("The movie will not be added due to the following reason(s):\n" + errorMessage);
        }else {
            return true;
        }
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres()
    {
        return genres;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A thief who enters the dreams of others to steal secrets must plant an idea into a target's subconscious.", List.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION, Genre.THRILLER, Genre.ALL)));

        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.", List.of(Genre.CRIME, Genre.DRAMA, Genre.ALL)));

        movies.add(new Movie("Titanic", "A young couple from different social classes fall in love aboard the doomed Titanic.", List.of(Genre.DRAMA, Genre.ROMANCE, Genre.HISTORY, Genre.ALL)));

        movies.add(new Movie("The Dark Knight", "Batman faces his greatest challenge as he battles the Joker's chaos in Gotham City.", List.of(Genre.ACTION, Genre.CRIME, Genre.DRAMA, Genre.THRILLER, Genre.ALL)));

        movies.add(new Movie("Forrest Gump", "The story of a man with a low IQ who witnesses and influences key historical events.", List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY, Genre.ALL)));

        movies.add(new Movie("Interstellar", "A team of explorers travels through a wormhole in space in search of a new habitable planet.", List.of(Genre.SCIENCE_FICTION, Genre.ADVENTURE, Genre.DRAMA, Genre.ALL)));

        movies.add(new Movie("The Conjuring", "Paranormal investigators help a family terrorized by a dark presence in their farmhouse.", List.of(Genre.HORROR, Genre.MYSTERY, Genre.THRILLER, Genre.ALL)));

        movies.add(new Movie("Gladiator", "A betrayed Roman general fights his way through the gladiatorial arena to seek revenge.", List.of(Genre.ACTION, Genre.DRAMA, Genre.HISTORY, Genre.ALL)));

        movies.add(new Movie("Finding Nemo", "A clownfish embarks on a journey to find his missing son with the help of a forgetful fish.", List.of(Genre.ANIMATION, Genre.ADVENTURE, Genre.COMEDY, Genre.FAMILY, Genre.ALL)));

        movies.add(new Movie("Schindler's List", "The true story of a businessman who saved over a thousand Jewish refugees during the Holocaust.", List.of(Genre.BIOGRAPHY, Genre.DRAMA, Genre.HISTORY, Genre.WAR, Genre.ALL)));

        return movies;
    }
}
