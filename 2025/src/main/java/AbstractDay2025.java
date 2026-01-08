import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractDay2025 {

    private String day;

    protected AbstractDay2025(String day) {
        this.day = day;
    }


    protected Path getPath() {
        ClassLoader classLoader = AbstractDay2025.class.getClassLoader();
        try {
            return Paths.get(classLoader.getResource(day + ".txt").toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    abstract void solution() throws Exception;
}
