package com.example.myapp.effect.FullColorLed;

public class EmitLedOnGameOver implements EmitFullColorLed {
    @Override
    public void emit() {
        for (int i = 0; i < tick; i++) {
            try {
                for (int j = 6; j <= 9; j++) {
                    fLed.on(j, 255, 255, 0);
                    Thread.sleep(timeoutOn);
                    fLed.off(j);
                    Thread.sleep(timeoutOff);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
