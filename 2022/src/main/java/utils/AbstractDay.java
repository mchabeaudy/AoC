package utils;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Path;
import java.nio.file.Paths;

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
