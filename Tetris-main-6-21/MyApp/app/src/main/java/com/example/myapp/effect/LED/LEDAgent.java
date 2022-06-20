package com.example.myapp.effect.LED;

import com.example.myapp.hw.LED;

public class LEDAgent {
    LED led = new LED();
    int index = 0b0000_0001;

    public void print() {
        led.print(index);
    }

    public void increment() {
        if (index < 0b1000_0000)
            index = (index << 1) | 0b1;
        led.print(index);
    }

    public void shutdownAll() {
        index = 0b0000_0000;
        led.print(index);
    }
}
