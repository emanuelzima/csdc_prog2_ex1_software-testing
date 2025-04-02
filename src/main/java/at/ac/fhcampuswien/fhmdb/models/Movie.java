package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String id;
    private String title;
    private String description;
    private List<Genre> genres;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors = new ArrayList<>();
    private List<String> writers = new ArrayList<>();
    private List<String> mainCast = new ArrayList<>();
    private double rating;

    @Override
    public String toString() {
        return this.title;
    }

    //Standard Konstruktor für Gson
    public Movie() {}

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public Movie(String id, String title, String description, List<Genre> genres, int releaseYear, String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast,double rating)
    {
            this.id = id;
            this.title = title;
            this.description = description;
            this.genres = genres;
            this.releaseYear = releaseYear;
            this.imgUrl = imgUrl;
            this.lengthInMinutes = lengthInMinutes;
            this.directors = directors;
            this.writers = writers;
            this.mainCast = mainCast;
            this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();

        // The Dark Knight
        movies.add(new Movie(
                "70638b3c-8a1e-414a-9a53-05ebf64078a2",
                "The Dark Knight",
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, the caped crusader must come to terms with one of the greatest psychological tests of his ability to fight injustice.",
                List.of(Genre.ACTION, Genre.CRIME, Genre.DRAMA),
                2008,
                "https://m.media-amazon.com/images/M/MV5BMTk4ODQzNDY3Ml5BMl5BanBnXkFtZTcwODA0NTM4Nw@@._V1_FMjpg_UX1000_.jpg",
                152,
                List.of("Christopher Nolan"),
                List.of("Jonathan Nolan", "Christopher Nolan"),
                List.of("Christian Bale", "Heath Ledger", "Aaron Eckhart"),
                9.0
        ));

        // The Lord of the Rings: The Return of the King
        movies.add(new Movie(
                "57803b54-5ab7-4c66-a994-0884bcd92b04",
                "The Lord of the Rings: The Return of the King",
                "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
                List.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY),
                2003,
                "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg",
                201,
                List.of("Peter Jackson"),
                List.of("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"),
                List.of("Elijah Wood", "Ian McKellen", "Viggo Mortensen"),
                8.9
        ));

        // Star Wars: Episode V - The Empire Strikes Back
        movies.add(new Movie(
                "a45e4b03-ece7-49e7-8144-4f2a6fe03432",
                "Star Wars: Episode V - The Empire Strikes Back",
                "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader and a bounty hunter named Boba Fett all over the galaxy.",
                List.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIENCE_FICTION),
                1980,
                "https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                124,
                List.of("Irvin Kershner"),
                List.of("Leigh Brackett", "Lawrence Kasdan", "George Lucas"),
                List.of("Mark Hamill", "Harrison Ford", "Carrie Fisher"),
                8.7
        ));

        // The Lord of the Rings: The Two Towers
        movies.add(new Movie(
                "fff085ba-1b76-4623-9c77-396017ed1e57",
                "The Lord of the Rings: The Two Towers",
                "While Frodo and Sam edge closer to Mordor with the help of the shifty Gollum, the divided fellowship makes a stand against Sauron's new ally, Saruman, and his hordes of Isengard.",
                List.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY),
                2002,
                "https://m.media-amazon.com/images/M/MV5BZTUxNzg3MDUtYjdmZi00ZDY1LTkyYTgtODlmOGY5N2RjYWUyXkEyXkFqcGdeQXVyMTA0MTM5NjI2._V1_FMjpg_UX1000_.jpg",
                179,
                List.of("Peter Jackson"),
                List.of("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"),
                List.of("Elijah Wood", "Ian McKellen", "Viggo Mortensen"),
                8.7
        ));

        // Schindler's List
        movies.add(new Movie(
                "521ba69d-f716-4a3d-a048-39cb05a685b1",
                "Schindler's List",
                "In German-occupied Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazi Germans.",
                List.of(Genre.BIOGRAPHY, Genre.DRAMA, Genre.HISTORY),
                1993,
                "https://m.media-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg",
                195,
                List.of("Steven Spielberg"),
                List.of("Steven Zaillian", "Thomas Keneally"),
                List.of("Liam Neeson", "Ralph Fiennes", "Ben Kingsley"),
                8.9
        ));

        // Goodfellas
        movies.add(new Movie(
                "45acd8b1-e4ed-47eb-b594-42695faebccf",
                "Goodfellas",
                "The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito in the Italian-American crime syndicate.",
                List.of(Genre.BIOGRAPHY, Genre.CRIME, Genre.DRAMA),
                1990,
                "https://m.media-amazon.com/images/M/MV5BY2NkZjEzMDgtN2RjYy00YzM1LWI4ZmQtMjIwYjFjNmI3ZGEwXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg",
                146,
                List.of("Martin Scorsese"),
                List.of("Nicholas Pileggi", "Nicholas Pileggi"),
                List.of("Robert De Niro", "Ray Liotta", "Joe Pesci"),
                8.7
        ));

        // Inception
        movies.add(new Movie(
                "86642997-ee66-4102-ade1-54941a1d3a6e",
                "Inception",
                "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                List.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION, Genre.THRILLER),
                2010,
                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg",
                148,
                List.of("Christopher Nolan"),
                List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
                8.8
        ));

        // The Matrix
        movies.add(new Movie(
                "e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION),
                1999,
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                136,
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                8.7
        ));

        return movies;
    }
}
