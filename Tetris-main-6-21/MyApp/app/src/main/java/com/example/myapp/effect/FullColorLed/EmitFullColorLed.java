package com.example.myapp.effect.FullColorLed;

import com.example.myapp.hw.FullColorLed;

public interface EmitFullColorLed {
    FullColorLed fLed = new FullColorLed();

    int tick = 6; // # cycles of emitting Full LEDs

    long timeoutOn = 100; // milliseconds
    long timeoutOff = 50; // milliseconds

    void emit();
}