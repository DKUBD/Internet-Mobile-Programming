package com.example.myapp.effect.TextLCD;

import com.example.myapp.hw.TextLCD;

public class TextLCDAgent {
    TextLCD tLCD = new TextLCD();

    public void print(String line1, String line2) {
        tLCD.print(line1, line2);
    }

    public void stop() {
        tLCD.stop();
    }
}
