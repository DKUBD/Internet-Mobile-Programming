package com.example.myapp;
import androidx.annotation.NonNull;
public class Coord {
    public int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Coord.class)
            return false;
        Coord c = (Coord) obj;
        return c.x == this.x && c.y == this.y;
    }
    @NonNull
    @Override
    public String toString() {
        return x + "," + y;
    }
}
