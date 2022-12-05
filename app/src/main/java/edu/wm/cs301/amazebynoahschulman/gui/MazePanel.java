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
import androidx.core.content.ContextCompat;

import org.w3c.dom.Attr;

import edu.wm.cs301.amazebynoahschulman.R;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;
import edu.wm.cs301.amazebynoahschulman.generation.MazeContainer;

/**
 * MazePanel custom view class
 * Currently, for P6, this custom view displays a custom bitmap image.
 * The image is square with a black rectangle on the bottom and a gray rectangle on top with a
 * red circle in the center of the square.
 */
public class MazePanel extends View implements P7PanelF22{
    /**
     * Field variable for the UIcanvas
     */
    private Canvas canvas;
    /**
     * Field variable for the bitmap
     */
    private Bitmap manual;
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
        manual = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        manual3 = Bitmap.createScaledBitmap(manual, 1000, 1000, true);
        paint = new Paint();
        canvas = new Canvas(manual3);

        //testMyImage(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(manual3, 0, 0, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(1000, 1000);
    }

    private void testMyImage(Canvas c) {
        canvas = c;

        // paint background, using 1 as placeholder value
        addBackground(1);

        // paint red ball
        setColor(Color.RED);
        addFilledOval(5, 5, 500, 500);

        // paint green ball
        setColor(115740);
        addFilledOval(10, 600, 300, 300);

        // paint yellow rectangle
        setColor(Color.YELLOW);
        addFilledRectangle(600, 5, 150, 200);

        // paint blue polygon
        // making array for x points and y points
        setColor(Color.BLUE);
        int[] xPoints = {600, 750, 950};
        int [] yPoints = {400, 750, 950};
        addFilledPolygon(xPoints, yPoints, 3);

        // printing a few lines
        setColor(Color.CYAN);
        addLine(200, 600, 600, 1000);
        // printing a few lines
        setColor(Color.WHITE);
        addLine(1000, 600, 600, 200);

        // adding an arc
        addArc(200, 850, 400, 200, 2700, 200);


    }

    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     */
    @Override
    public void commit() {
        invalidate();
    }

    /**
     * Tells if instance is able to draw. This ability depends on the
     * context, for instance, in a testing environment, drawing
     * may be not possible and not desired.
     * Substitute for code that checks if graphics object for drawing is not null.
     * @return true if drawing is possible, false if not.
     */
    @Override
    public boolean isOperational() {
        if (canvas == null) {
            return false;
        }
        return true;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    /**
     * Draws two solid rectangles to provide a background.
     * Note that this also erases any previous drawings.
     * The color setting adjusts to the distance to the exit to
     * provide an additional clue for the user.
     * Colors transition from black to gold and from grey to green.
     * Substitute for FirstPersonView.drawBackground method.
     * @param percentToExit gives the distance to exit
     */
    @Override
    public void addBackground(float percentToExit) {
        // top color set to GRAY
        setColor(Color.GRAY);
        // now draw rectangle that takes up bottom half of the MazePanel
        addFilledRectangle(0,0, 1000, 500);
        // bottom color set to BLACK
        setColor(Color.BLACK);
        addFilledRectangle(0,500, 1000, 500);
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
        canvas.drawRect((float)x, (float)y, (float)x + (float)width, (float)y + (float)height, paint);
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
        for (int i = 1; i < nPoints; i++) {
            polygonPath.lineTo(xPoints[i], yPoints[i]);
        }
        // then use lineTo for the first point
        polygonPath.lineTo(xPoints[0], yPoints[0]);
        // then draw polygon on canvas
        canvas.drawPath(polygonPath, paint);
    }

    /**
     * Adds a polygon.
     * The polygon is not filled.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.drawPolygon method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // make the Paint style stroke
        paint.setStyle(Paint.Style.STROKE);
        // will create a polygon by using Path object
        Path polygonPath = new Path();
        polygonPath.reset();
        // using moveTo method for the first point
        polygonPath.moveTo(xPoints[0], yPoints[0]);
        // for the rest of the points, use lineTo method
        for (int i = 1; i < nPoints; i++) {
            polygonPath.lineTo(xPoints[i], yPoints[i]);
        }
        // then use lineTo for the first point
        polygonPath.lineTo(xPoints[0], yPoints[0]);
        // then draw polygon on canvas
        canvas.drawPath(polygonPath, paint);
    }

    /**
     * Adds a line.
     * A line is described by {@code (x,y)} coordinates for its
     * starting point and its end point.
     * Substitute for Graphics.drawLine method
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        paint.setStrokeWidth(6);
        canvas.drawLine(startX, startY, endX, endY, paint);
        paint.setStrokeWidth(1);
    }

    /**
     * Adds a filled oval.
     * The oval is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis. An oval is
     * described like a rectangle.
     * Substitute for Graphics.fillOval method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        // set paint type to fill
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval((float)x, (float)y, (float)x + (float)width, (float)y + (float)height, paint);
    }

    /**
     * Adds the outline of a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees,
     * using the current color. Angles are interpreted such that 0 degrees
     * is at the 3 o'clock position. A positive value indicates a counter-clockwise
     * rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is
     * (x, y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width + 1 pixels wide
     * by height + 1 pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the start
     * and end of the arc segment will be skewed farther along the longer
     * axis of the bounds.
     * Substitute for Graphics.drawArc method
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc((float)x, (float)y, (float)x + (float)width, (float)y + (float)height, startAngle, arcAngle, false, paint);
    }

    /**
     * Adds a string at the given position.
     * Substitute for CompassRose.drawMarker method
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText(str, x, y, paint);
    }

    /**
     * Sets the value of a single preference for the rendering algorithms.
     * It internally maps given parameter values into corresponding java.awt.RenderingHints
     * and assigns that to the internal graphics object.
     * Hint categories include controls for rendering quality
     * and overall time/quality trade-off in the rendering process.
     *
     * Refer to the awt RenderingHints class for definitions of some common keys and values.
     *
     * Note for Android: start with an empty default implementation.
     * Postpone any implementation efforts till the Android default rendering
     * results in unsatisfactory image quality.
     *
     * @param hintKey the key of the hint to be set.
     * @param hintValue the value indicating preferences for the specified hint category.
     */
    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {
    }


}

