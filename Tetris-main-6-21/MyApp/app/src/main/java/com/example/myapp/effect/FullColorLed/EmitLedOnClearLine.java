package com.example.myapp.effect.FullColorLed;

public class EmitLedOnClearLine implements EmitFullColorLed {
    @Override
    public void emit() {
        for (int i = 0; i < tick; i++) {
            try {
                for (int j = 9; j >= 5; j--) {
                    fLed.on(j, 0, 255, 0);
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
