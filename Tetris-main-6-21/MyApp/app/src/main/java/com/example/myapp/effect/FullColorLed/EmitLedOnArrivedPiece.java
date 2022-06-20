package com.example.myapp.effect.FullColorLed;

public class EmitLedOnArrivedPiece implements EmitFullColorLed {
    int pieceColor = 0;

    public EmitLedOnArrivedPiece(int color) {
        if (color >= 0 || color <= 255)
            this.pieceColor = color;
    }

    @Override
    public void emit() {
        for (int i = 0; i < tick - 4; i++) {
            try {
                for (int j = 9; j >= 5; j -= 2) {
                    fLed.on(j, pieceColor, pieceColor, pieceColor);
                    fLed.on(j - 1, pieceColor, pieceColor, pieceColor);
                    Thread.sleep(timeoutOn);
                    fLed.off(j);
                    fLed.off(j - 1);
                    Thread.sleep(timeoutOff);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
