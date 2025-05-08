package at.ac.fhcampuswien.fhmdb.exceptions;

public class DatabaseException extends Exception{
    public DatabaseException(String m, Throwable c)
    {
        super(m, c);
    }

    public DatabaseException(Throwable c)
    {
        super(c);
    }
}
