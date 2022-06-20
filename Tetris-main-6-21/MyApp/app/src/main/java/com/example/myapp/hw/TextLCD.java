package com.example.myapp.hw;

import static com.example.myapp.GlobalNative.lcdClear;
import static com.example.myapp.GlobalNative.lcdPrint;

public class TextLCD {

    public void print(String line1, String line2) {
        lcdClear();
        lcdPrint(0, line1);
//        lcdPrint(1, line2);
    }

    public void stop() {
        lcdClear();
    }
}