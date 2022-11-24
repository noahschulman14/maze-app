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

/**
 * MazePanel custom view class
 * Currently, for P6, this custom view displays a custom bitmap image.
 * The image is square with a black rectangle on the bottom and a gray rectangle on top with a
 * red circle in the center of the square.
 */
public class MazePanel extends View {
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
     * MazePanel constructor 1
     * @param context
     */
    public MazePanel(Context context) {
        super(context);
        init(null);
    }

    /**
     * MazePanel constructor 2
     * @param context
     * @param attrs
     */
    public MazePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    /**
     * MazePanel constructor 3
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    /**
     * MazePanel constructor 4
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(null);
    }

    /**
     * Initialization helper method, initializes the field variables
     * @param set
     */
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
