package at.ac.fhcampuswien.fhmdb.patterns;

public class Factory {
    private static final MyFactory INSTANCE = new MyFactory();

    public static MyFactory getFactory()
    {
        return INSTANCE;
    }
}
