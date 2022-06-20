package com.example.myapp.dto;

import java.util.ArrayList;

public class Setting {
    final long lineScore;
    final ArrayList<ArrayList<Integer>> timeLevels;
    final int width;
    final int height;

    public Setting(long lineScore,
                   ArrayList<ArrayList<Integer>> timeLevels, int width, int height) {
        this.lineScore = lineScore;
        this.timeLevels = timeLevels;
        this.width = width;
        this.height = height;
    }

    public long getLineScore() {
        return lineScore;
    }

    public ArrayList<ArrayList<Integer>> getTimeLevels() {
        return timeLevels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
