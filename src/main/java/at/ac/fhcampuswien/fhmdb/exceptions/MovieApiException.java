package at.ac.fhcampuswien.fhmdb.exceptions;

public class MovieApiException extends Exception{
    public MovieApiException(String m, Throwable c)
    {
        super(m, c);
    }

    public MovieApiException(Throwable c)
    {
        super(c);
    }
}
