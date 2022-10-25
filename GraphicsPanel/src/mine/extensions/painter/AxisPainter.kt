package mine.extensions.painter

import java.awt.Color
import java.awt.Graphics
import mine.extensions.converter.PixelCoordConverter
import java.awt.Point
import java.lang.Math.abs
import java.lang.Math.pow
import kotlin.math.sign

object AxisPainter : Painter {


    var xMin: Double = 0.0
        set(value) {
            field = value;
            _converter.xMin = value;
        }

    var xMax: Double = 0.0
        set(value) {
            field = value;
            _converter.xMax = value;
        }

    var yMin: Double = 0.0
        set(value) {
            field = value;
            _converter.yMin = value;
        }

    var yMax: Double = 0.0
        set(value) {
            field = value;
            _converter.yMax = value;
        }

    override var width: Int = 0
        set(value) {
            field = value;
            _converter.width = value;
        }
    override var height: Int = 0
        set(value) {
            field = value;
            _converter.height = value;
        }

    override var isVisible: Boolean = true;


    private val _converter = PixelCoordConverter;

    init {
        _converter.width = width;
        _converter.height = height;

        _converter.xMin = this.xMin;
        _converter.xMax = this.xMax;
        _converter.yMin = this.yMin;
        _converter.yMax = this.yMax;

//        PixelCoordConverter.xLimits = Pair(xMin, xMax);
//        PixelCoordConverter.yLimits = Pair(yMin, yMax);
    }

    override fun getName(): String = "AxisPainter";

    override fun paint(g: Graphics?) {
        paintAxes(g);
        paintScale(g);
        paintNumbers(g);
    }


    private fun paintAxes(g: Graphics?) =
        g?.apply {

            val p1 = Point(_converter.mapXCoordToPixel(0.0), _converter.mapYCoordToPixel(yMin));
            val p2 = Point(_converter.mapXCoordToPixel(0.0), _converter.mapYCoordToPixel(yMax));

            val p3 = Point(_converter.mapXCoordToPixel(xMin), _converter.mapYCoordToPixel(0.0));
            val p4 = Point(_converter.mapXCoordToPixel(xMax), _converter.mapYCoordToPixel(0.0));

            color = Color(85, 91, 110);

            drawLine(p1.x, p1.y, p2.x, p2.y);
            drawLine(p3.x, p3.y, p4.x, p4.y);
        }


    private fun paintScale(g: Graphics?) =  // TODO: make dynamic scaling
        g?.apply {

            val x0 = _converter.mapXCoordToPixel(0.0);
            val y0 = _converter.mapYCoordToPixel(0.0);

            // x axis
            val left = (_converter.xMin * 10).toInt();
            val right = (_converter.xMax * 10).toInt();

            val z = abs(right - left).toString().length;

            val leftOrder = (abs(left).toString().length) - 1;
            val rightOrder = (abs(right).toString().length) - 1;

            val leftAbs = abs(left) * 10;
            val rightAbs = abs(right) * 10;

            val leftSign = sign(left.toDouble());
            val rightSign = sign(right.toDouble());

            val smallScale = (leftAbs / 5);
            val bigScale = (leftAbs / 10);
            val inc = (smallScale / 5.0);


//            var i = 0;
//            while (i != leftAbs) // TODO: ПОЛОЖИТЕЛЬНАЯ НЕ РАБОТАЕТ
//            {
//                color = Color(85, 91, 110);
//                var size = 2;
//
//                if (i % smallScale == 0) {
//                    size += 1;
//                    color = Color(166, 161, 87);
//                }
//                if (i % bigScale == 0)
//                    size += 1;
//
//
//                val tt = leftSign * i / 100.0;
//                val x = _converter.mapXCoordToPixel(leftSign * i / 100.0)
//
//                drawLine(x, y0 - size, x, y0 + size);
//
////                if (i % smallScale == 0 || i % bigScale == 0) {
////                    val x = _converter.mapXCoordToPixel(leftSign * i / 10.0)
////
////                    drawLine(x, y0 - size, x, y0 + size);
////                }
//                i += inc.toInt();
//            }
//
//
//
//            i = 0;
//            while (i != rightAbs)
//            {
//                color = Color(85, 91, 110);
//                var size = 2;
//
//                if (i % smallScale == 0) {
//                    size += 1;
//                    color = Color(166, 161, 87);
//                }
//                if (i % bigScale == 0)
//                    size += 1;
//
//
//                val tt = leftSign * i / 100.0;
//                val x = _converter.mapXCoordToPixel(rightSign * i / 100.0)
//
//                drawLine(x, y0 - size, x, y0 + size);
//
////                if (i % smallScale == 0 || i % bigScale == 0) {
////                    val x = _converter.mapXCoordToPixel(leftSign * i / 10.0)
////
////                    drawLine(x, y0 - size, x, y0 + size);
////                }
//                i += inc.toInt();
//            }

// -----------------------------------------------------------------------------------
//            for (i in 0..leftAbs) {
//
//                color = Color(85, 91, 110);
//                var size = 2;
//
//                if (i % smallScale == 0) {
//                    size += 1;
//                    color = Color(166, 161, 87);
//                }
//                if (i % bigScale == 0)
//                    size += 1;
//
//
//                val x = _converter.mapXCoordToPixel(leftSign * i / 10.0)
//
//                drawLine(x, y0 - size, x, y0 + size);
//
////                if (i % smallScale == 0 || i % bigScale == 0) {
////                    val x = _converter.mapXCoordToPixel(leftSign * i / 10.0)
////
////                    drawLine(x, y0 - size, x, y0 + size);
////                }
//                i+=smallScale;
//
//            }

            for (i in left..right) {

                color = Color(85, 91, 110);
                var size = 2;

                if (i % ((leftOrder/2.0)*10).toInt() == 0) {
                    size += 1;
                    color = Color(166, 161, 87);
                }
                if (i % 10 == 0)
                    size += 1;

                val x = _converter.mapXCoordToPixel(i / 10.0)

                drawLine(x, y0 - size, x, y0 + size);
            }

            // y axis
            val bottom = (_converter.yMin * 10).toInt();
            val top = (_converter.yMax * 10).toInt();

            for (i in bottom..top) {
                color = Color(85, 91, 110);
                var d = 2;

                if (i % 5 == 0) {
                    d += 1;
                    color = Color(166, 161, 87);
                }
                if (i % 10 == 0)
                    d += 1;

                val y = _converter.mapYCoordToPixel(i / 10.0)
                drawLine(x0 - d, y, x0 + d, y);
            }
        }


    private fun paintNumbers(g: Graphics?) =
        g?.apply {

            val x0 = _converter.mapXCoordToPixel(0.0);
            val y0 = _converter.mapYCoordToPixel(0.0);

            color = Color(137, 176, 174);

            // x axis
            val left = (_converter.xMin * 10).toInt();
            val right = (_converter.xMax * 10).toInt();

            val xDx = -8;
            val xDy = 15;

            for (i in left..right) {

                if (i % 5 == 0 && i != 0) {
                    val x = _converter.mapXCoordToPixel(i / 10.0);
                    drawString((i / 10.0).toString(), x + xDx, y0 + xDy)
                }

            }

            // y axis
            val bottom = (_converter.yMin * 10).toInt();
            val top = (_converter.yMax * 10).toInt();

            val yDx = 5;
            val yDy = 5;

            for (i in bottom..top) {

                if (i % 5 == 0 && i != 0) {
                    val y = _converter.mapYCoordToPixel(i / 10.0)
                    drawString((i / 10.0).toString(), x0 + yDx, y + yDy)
                }
            }

        }


}