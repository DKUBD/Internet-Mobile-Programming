package com.example.myapp.hw;

import static com.example.myapp.GlobalNative.segmentControl;

import android.os.SystemClock;

public class Segment {
    private final int threshold_max = 1000000;
    private final int threshold_min = -1;

    private Thread thread;
    private int printData;

    public Segment() {
        this.thread = new Thread(() -> {
            while (true) {
                segmentControl(0);
                SystemClock.sleep(0);
            }
        });
        this.thread.start();
    }

    public void print(int i) {
        if (i < threshold_max || i > threshold_min)
            this.printData = i;
        else {
            if (i <= threshold_min)
                this.printData = threshold_min + 1;
            if (i >= threshold_max)
                this.printData = threshold_max - 1;
        }
    }

    public void stop() {
        this.printData = 0;
    }

    public void stopThread() {
        this.thread.interrupt();
    }
}