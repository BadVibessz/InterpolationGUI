package mine.extensions.converter

import extensions.eq
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.max

object PixelCoordConverter {

    // TODO: SAFE SETTER
    var xMin: Double = 0.0
//        get() = field;
//        set(value) {
//            if (value >= xMax)
//                field = xMax - 0.1;
//        }

    var xMax: Double = 0.0
//        get() = field;
//        set(value) {
//            if (value <= xMin)
//                field = xMin + 0.1;
//        }

    var yMin: Double = 0.0
//        get() = field;
//        set(value) {
//            if (value >= yMax)
//                field = yMax - 0.1;
//        }

    var yMax: Double = 0.0
//        get() = field;
//        set(value) {
//            if (value <= yMin)
//                field = yMin + 0.1;
//        }


    var width: Int = 1
        get() = field;
        set(value) {
            field = max(1, value);
        }

    var height: Int = 1
        get() = field;
        set(value) {
            field = max(1, value);
        }


    var xLimits: Pair<Double, Double>
        get() = Pair(xMin, xMax);
        set(value) {
            if (value.first < value.second) {
                xMin = value.first;
                xMax = value.second;
            } else if (value.first > value.second) {
                xMin = value.second;
                xMax = value.first;
            } else if (value.first eq value.second) {
                xMin -= 0.1;
                xMax += 0.1;
            }
        }

    var yLimits: Pair<Double, Double>
        get() = Pair(yMin, yMax);
        set(value) {
            if (value.first < value.second) {
                yMin = value.first;
                yMax = value.second;
            } else if (value.first > value.second) {
                yMin = value.second;
                yMax = value.first;
            } else if (value.first eq value.second) {
                yMin -= 0.1;
                yMax += 0.1;
            }
        }

    val xScale: Double
        get() = width / (xMax - xMin);
    val yScale: Double
        get() = height / (yMax - yMin);

    init {
        this.width = width;
        this.height = height;

        xLimits = Pair(xMin, xMax);
        yLimits = Pair(yMin, yMax);
    }


    fun mapXCoordToPixel(x: Double) =
        ((x - xMin) * xScale).coerceIn(-1.0 * (width - 1), 2.0 * (width - 1)).toInt();


    fun mapYCoordToPixel(y: Double) =
        ((yMax - y) * yScale).coerceIn(-1.0 * (height - 1), 2.0 * (height - 1)).toInt();

    fun mapXPixelToCoord(x: Int) =
        x / xScale + xMin;

    fun mapYPixelToCoord(y: Int) =
        yMax - y / yScale;

    fun mapCoordToPixel(point: Point2D): Point2D =
        Point(mapXCoordToPixel(point.x), mapYCoordToPixel(point.y));

    fun mapPixelToCoord(point: Point): Point2D =
        Point2D.Double().also { it.setLocation(mapXPixelToCoord(point.x), mapYPixelToCoord(point.y)); };


}