import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

public abstract class AbstractDay {

    private String day;

    protected AbstractDay(String day) {
        this.day = day;
    }


    protected Path getPath() {
        return Paths.get(getSystemResource(day + ".txt").getFile()).toAbsolutePath();
    }

    abstract void solution() throws Exception;
}
