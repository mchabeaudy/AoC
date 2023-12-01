import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

public abstract class AbstractDay {

    private String day;

    public AbstractDay(String day) {
        this.day = day;
    }


    protected Path getPath() {
        return Paths.get(getSystemResource(day + ".txt").getFile()).toAbsolutePath();
    }

    public abstract void solution() throws Exception;
}
