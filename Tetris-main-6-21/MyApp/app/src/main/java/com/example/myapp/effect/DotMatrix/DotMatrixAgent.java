package com.example.myapp.effect.DotMatrix;

import com.example.myapp.hw.DotMatrix;

public class DotMatrixAgent {
    DotMatrix dMatrix = new DotMatrix();

    public void print(String message, int speed, int printCount) {
        dMatrix.print(message, speed, printCount);
    }

    public void stop() {
        dMatrix.stop();
    }
}
