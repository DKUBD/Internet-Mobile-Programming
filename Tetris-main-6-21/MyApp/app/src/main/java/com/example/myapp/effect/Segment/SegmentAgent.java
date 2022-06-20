package com.example.myapp.effect.Segment;

import com.example.myapp.hw.Segment;

public class SegmentAgent {
    Segment segment;

    public SegmentAgent() {
        segment = new Segment();
    }

    public void print(int score) {
        segment.print(score);
    }

    public void stop() {
        segment.stop();
    }

    public void stopThread() {
        segment.stopThread();
    }
}
