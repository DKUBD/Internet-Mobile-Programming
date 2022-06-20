package com.example.myapp.gameplay;


import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class BoardTimer {
    public static final int MAX_INTERVAL = 1200;
    public static final int MIN_INTERVAL = 200;

    private final Handler handler;
    private final Queue<Pair<Integer, Integer>> levels;

    private Timer timer = null;
    private int elapsedTime;
    private int dropInterval;

    public BoardTimer(Handler handler, Queue<Pair<Integer, Integer>> levels) {
        this.handler = handler;
        this.levels = levels;
        if (levels.isEmpty())
            changeSpeed(MAX_INTERVAL);
        else {
            assert levels.peek() != null;
            changeSpeed(levels.peek().second);
        }
    }

    public void start() {
        pause();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage());
                elapsedTime += dropInterval;
                if (!levels.isEmpty()) {
                    assert levels.peek() != null;
                    if (elapsedTime >= levels.peek().first * 1000) {
                        Pair<Integer, Integer> level = levels.remove();
                        changeSpeed(level.second);
                    }
                }
            }
        }, 0, dropInterval);
    }

    public void pause() {
        try {
            timer.cancel();
        } catch (NullPointerException e) {
            Log.e("BoardTimer", "Hasn't been start");
        }
    }

    void changeSpeed(int speed) {
        dropInterval = Math.min(MAX_INTERVAL, Math.max(MAX_INTERVAL - speed, MIN_INTERVAL));
        Log.d("BoardTimer", "Speed: " + dropInterval);
        pause();
        start();
    }
}
