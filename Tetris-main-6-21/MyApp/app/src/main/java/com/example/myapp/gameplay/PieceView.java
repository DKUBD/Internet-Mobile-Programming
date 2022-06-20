package com.example.myapp.gameplay;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieceView extends View {
    PieceType nextPiece = null;

    float unit;
    final Paint paint = new Paint();
    final Paint borderPaint = new Paint();
    Canvas canvas;
    // margin
    float mw, mh;

    public PieceView(Context context) {
        super(context);
        init();
    }

    public PieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieceView(Context context, AttributeSet attrs, int values) {
        super(context, attrs, values);
        init();
    }

    private void init() {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);
        borderPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (nextPiece == null)
            return;

        unit = Math.min(getWidth() / 4f, getHeight() / 2f);
        mw = (getWidth() - unit * nextPiece.width) / 2;
        mh = (getHeight() - unit * nextPiece.height) / 2;
        this.canvas = canvas;
        paint.setColor(nextPiece.color);

        for (int n : nextPiece.getData()) {
            int x = (n < 4) ? 0 : 1;
            int y = n % 4;
            drawUnit(x, y, 0, paint);
            drawUnit(x, y, -1, borderPaint);
        }
    }

    @Override
    protected void onMeasure(int measuredWidth, int measuredHeight) {
        super.onMeasure(measuredWidth, measuredHeight);
        int w = getMeasuredWidth();
        this.setMeasuredDimension(w, w);
    }

    private void drawUnit(float x, float y, float pad, Paint paint) {
        // Row-column shenanigan
        x = x * unit + mh;
        y = y * unit + mw;
        float r = unit / 5;

        RectF rF = new RectF(y + pad, x + pad, y + unit - pad, x + unit - pad);
        canvas.drawRoundRect(rF, r, r, paint);
    }

    public void setPiece(PieceType piece) {
        nextPiece = piece;
        invalidate();
    }
}
