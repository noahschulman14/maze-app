package edu.wm.cs301.amazebynoahschulman.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.w3c.dom.Attr;

import edu.wm.cs301.amazebynoahschulman.R;

public class MazePanel extends View {

    private Canvas canvas;

    private Bitmap manual;
    private Bitmap manual2;
    private Bitmap manual3;

    private Paint paint;

    public MazePanel(Context context) {
        super(context);



        init(null);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(null);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(null);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(null);
    }

    private void init(@Nullable AttributeSet set) {
        manual = BitmapFactory.decodeResource(getResources(), R.drawable.manual_test_image);
        manual2 = manual.copy(Bitmap.Config.ARGB_8888, true);
        manual3 = Bitmap.createScaledBitmap(manual2, 700, 700, true);
        canvas = new Canvas(manual3);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(manual3, 0, 0, paint);
    }


}
