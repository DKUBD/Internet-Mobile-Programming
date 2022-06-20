package com.example.myapp.gameplay;

import com.example.myapp.R;

public enum PieceType {
    SQUARE(0, 1, 4, 5, R.color.yellow_block),
    L(2, 4, 5, 6, R.color.green_block),
    LR(0, 1, 2, 6, R.color.purple_block),
    STRAIGHT(0, 1, 2, 3, R.color.teal_block),
    Z(0, 1, 5, 6, R.color.red_block),
    ZR(1, 2, 4, 5, R.color.light_blue_block),
    T(1, 4, 5, 6, R.color.orange_block);

    private final int[] data = new int[4];
    public final int width, height;
    public final int color;

    PieceType(int b1, int b2, int b3, int b4, int color) {
        data[0] = b1;
        data[1] = b2;
        data[2] = b3;
        data[3] = b4;
        height = (b4 > 3) ? 2 : 1;
        width = b4 % 4 + 1;
        this.color = color;
    }

    public int[] getData() {
        return data;
    }
}
