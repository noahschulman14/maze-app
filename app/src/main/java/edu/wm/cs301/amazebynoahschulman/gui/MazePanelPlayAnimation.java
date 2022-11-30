package edu.wm.cs301.amazebynoahschulman.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import edu.wm.cs301.amazebynoahschulman.R;

/**
 * MazePanelPlayAnimation custom view
 * Only for P6, displays a bitmap image of a square with a black rectangle on bottom and
 * gray rectangle on top with a green maze wall and a yellow maze wall.
 */
public class MazePanelPlayAnimation extends View {
    /**
     * Field variable for the canvas
     */
    private Canvas canvas;
    /**
     * Field variable for the bitmap
     */
    private Bitmap manual;
    /**
     * Field variable for mutable bitmap
     */
    private Bitmap manual2;
    /**
     * Field variable for scaled bitmap
     */
    private Bitmap manual3;
    /**
     * Field variable for the paint
     */
    private Paint paint;

    /**
     * MazePanelPlayAnimation constructor 1
     * @param context
     */
    public MazePanelPlayAnimation(Context context) {
        super(context);



        init(null);
    }

    /**
     * MazePanelPlayAnimation constructor 2
     * @param context
     * @param attrs
     */
    public MazePanelPlayAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(null);
    }

    /**
     * MazePanelPlayAnimation constructor 3
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MazePanelPlayAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(null);
    }

    /**
     * MazePanelPlayAnimation constructor 4
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public MazePanelPlayAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(null);
    }

    /**
     * Initialization helper method. Initializes field variables.
     * @param set
     */
    private void init(@Nullable AttributeSet set) {
        manual = BitmapFactory.decodeResource(getResources(), R.drawable.animation_test);
        manual2 = manual.copy(Bitmap.Config.ARGB_8888, true);
        manual3 = Bitmap.createScaledBitmap(manual2, 700, 700, true);
        canvas = new Canvas(manual3);
        paint = new Paint();

        // need to create a Path
        // move to and line to method

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(manual3, 0, 0, paint);
    }
}
