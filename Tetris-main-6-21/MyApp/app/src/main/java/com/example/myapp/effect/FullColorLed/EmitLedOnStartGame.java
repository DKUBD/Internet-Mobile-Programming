package com.example.myapp.effect.FullColorLed;

public class EmitLedOnStartGame implements EmitFullColorLed {
    @Override
    public void emit() {
        for (int i = 0; i < tick; i++) {
            try {
                fLed.on(9, 255, 255, 0);
                Thread.sleep(timeoutOn);
                fLed.off(9);
                Thread.sleep(timeoutOff);

                fLed.on(8, 255, 255, 0);
                Thread.sleep(timeoutOn);
                fLed.off(8);
                Thread.sleep(timeoutOff);

                fLed.on(7, 255, 255, 255);
                Thread.sleep(timeoutOn);
                fLed.off(7);
                Thread.sleep(timeoutOff);

                fLed.on(6, 255, 255, 0);
                Thread.sleep(timeoutOn);
                fLed.off(6);
                Thread.sleep(timeoutOff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}