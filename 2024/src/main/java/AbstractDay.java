import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractDay {

    private String day;

    protected AbstractDay(String day) {
        this.day = day;
    }


    protected Path getPath() {
        ClassLoader classLoader = AbstractDay.class.getClassLoader();
        try {
            return Paths.get(classLoader.getResource(day + ".txt").toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    abstract void solution() throws Exception;
}
