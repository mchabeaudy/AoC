package day9;

import static java.lang.Math.abs;

import java.util.Objects;

public class Point {

    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    public Point(Point p) {
        this(p.x, p.y);
    }


    public void moveTo(Point head) {
        int dx = head.x - x;
        int dy = head.y - y;
        switch (dx) {
            case 2, -2 -> {
                x += dx / abs(dx);
                switch (dy) {
                    case 2, -2 -> y = head.y - abs(dy) / dy;
                    case 1, -1, 0 -> y = head.y;
                }
            }
            case 1, -1 -> {
                if (abs(dy) > 1) {
                    x = head.x;
                    y += dy / abs(dy);
                }
            }
            case 0 -> {
                if (abs(dy) > 1) {
                    y += dy / abs(dy);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }

}
