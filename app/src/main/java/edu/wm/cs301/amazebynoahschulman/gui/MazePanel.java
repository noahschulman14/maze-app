package edu.wm.cs301.amazebynoahschulman.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
public class MazePanel extends View implements P7PanelF22{
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


    @Override
    public void commit() {

    }

    @Override
    public boolean isOperational() {
        return false;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit) {
        // SEE FirstPersonView drawBackground method
    }

    /**
     * Adds a filled rectangle.
     * The rectangle is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis.
     * Substitute for Graphics.fillRect() method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        // first need to make the Paint fill
        paint.setStyle(Paint.Style.FILL);
        // now need to put a rectangle in specified part of canvas
        canvas.drawRect((float)x, (float)y, (float)x + width, (float)y + height, paint);
        //                                                          ******SHOULD THIS BE Y - HEIGHT??? ******
    }

    /**
     * Adds a filled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.fillPolygon() method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // first need to make the Paint fill
        paint.setStyle(Paint.Style.FILL);
        // will create a polygon by using Path object
        Path polygonPath = new Path();
        polygonPath.reset();
        // using moveTo method for the first point
        polygonPath.moveTo(xPoints[0], yPoints[0]);
        // for the rest of the points, use lineTo method
        for (int i = 1; i < xPoints.length; i++) {
            polygonPath.lineTo(xPoints[i], yPoints[i]);
        }
        // then use lineTo for the first point
        polygonPath.lineTo(xPoints[0], yPoints[0]);
        // then draw polygon on canvas
        canvas.drawPath(polygonPath, paint);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {

    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void addMarker(float x, float y, String str) {

    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {

    }
}

