package mine.extensions.painter

import java.awt.Color
import java.awt.Graphics
import mine.extensions.converter.PixelCoordConverter
import java.awt.Point
import java.lang.Math.abs

object AxisPainter : Painter {


    override var isVisible: Boolean = true;
    private val _converter = PixelCoordConverter;


    override var name: String = "AxisPainter";
    override fun paint(g: Graphics?) {
        paintAxes(g);
        paintScale(g);
        paintNumbers(g);
    }


    private fun paintAxes(g: Graphics?) =
        g?.apply {

            val p1 = Point(_converter.mapXCoordToPixel(0.0), _converter.mapYCoordToPixel(_converter.yMin));
            val p2 = Point(_converter.mapXCoordToPixel(0.0), _converter.mapYCoordToPixel(_converter.yMax));

            val p3 = Point(_converter.mapXCoordToPixel(_converter.xMin), _converter.mapYCoordToPixel(0.0));
            val p4 = Point(_converter.mapXCoordToPixel(_converter.xMax), _converter.mapYCoordToPixel(0.0));

            color = Color(85, 91, 110);

            drawLine(p1.x, p1.y, p2.x, p2.y);
            drawLine(p3.x, p3.y, p4.x, p4.y);
        }


    private fun paintScale(g: Graphics?) =
        g?.apply {

            val x0 = _converter.mapXCoordToPixel(0.0);
            val y0 = _converter.mapYCoordToPixel(0.0);

            // x axis
            val left = (_converter.xMin * 10).toInt();
            val right = (_converter.xMax * 10).toInt();

            val leftAbs = abs(left) * 100;
            val rightAbs = abs(right) * 100;


            val xSmallScale = (leftAbs / 5);
            val xBigScale = (leftAbs / 10);
            val xInc = (xSmallScale / 5.0);


            var i = left * 100;
            while (i <= right * 100) // TODO
            {
                color = Color(85, 91, 110);
                var size = 2;

                if (i % xSmallScale == 0) {
                    size += 1;
                    color = Color(166, 161, 87);
                }
                if (i % xBigScale == 0)
                    size += 1;

                val x = _converter.mapXCoordToPixel(i / 1000.0)
                drawLine(x, y0 - size, x, y0 + size);

                i += xInc.toInt();
            }


            // y axis
            val bottom = (_converter.yMin * 10).toInt();
            val top = (_converter.yMax * 10).toInt();

            val bottomAbs = abs(bottom) * 100;
            val topAbs = abs(top) * 100;


            val ySmallScale = (bottomAbs / 5);
            val yBigScale = (bottomAbs / 10);
            val yInc = (ySmallScale / 5.0);


            var j = bottom * 100;
            while (j <= top * 100) // TODO
            {
                color = Color(85, 91, 110);
                var size = 2;

                if (j % ySmallScale == 0) {
                    size += 1;
                    color = Color(166, 161, 87);
                }
                if (j % yBigScale == 0)
                    size += 1;

                val y = _converter.mapYCoordToPixel(j / 1000.0)
                drawLine(x0 - size, y, x0 + size, y);

                j += yInc.toInt();
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

            val leftAbs = abs(left) * 100;
            val rightAbs = abs(right) * 100;


            val xSmallScale = (leftAbs / 5);
            val xBigScale = (leftAbs / 10);
            val xInc = (xSmallScale / 5.0);

            val xDx = -8;
            val xDy = 15;

            var i = left * 100;
            while (i <= right * 100) // TODO
            {
                if (i % xSmallScale == 0 && i != 0) {

                    val x = _converter.mapXCoordToPixel(i / 1000.0);
                    drawString((i / 1000.0).toString(), x + xDx, y0 + xDy);

                }
                i += xInc.toInt();

            }

            // y axis
            val bottom = (_converter.yMin * 10).toInt();
            val top = (_converter.yMax * 10).toInt();

            val bottomAbs = abs(bottom) * 100;
            val topAbs = abs(top) * 100;


            val ySmallScale = (bottomAbs / 5);
            val yBigScale = (bottomAbs / 10);
            val yInc = (ySmallScale / 5.0);

            val yDx = 5;
            val yDy = 5;

            var j = bottom * 100;
            while (j <= top * 100) {
                if (j % ySmallScale == 0 && j != 0) {

                    val y = _converter.mapYCoordToPixel(j / 1000.0)
                    drawString((j / 1000.0).toString(), x0 + yDx, y + yDy)

                }
                j += yInc.toInt();
            }

        }


}