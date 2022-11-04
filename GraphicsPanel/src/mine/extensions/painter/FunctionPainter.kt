package mine.extensions.painter

import mine.extensions.converter.PixelCoordConverter
import java.awt.*
import java.awt.geom.Point2D


class FunctionPainter : Painter {

    private val _converter = PixelCoordConverter;

    var FunctionColor: Color = Color.BLACK;
    var function: ((Double) -> Double)? = null


    override fun paint(g: Graphics?) {

        if (function == null) return
        if (!isVisible) return;


        g as Graphics2D?;


        g?.apply {


            color = FunctionColor;
            setStroke(BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)) // todo: understand why fps drops


            val points = mutableListOf<Point2D>();
            var x: Double;
            for (i in 0.._converter.width) {
                x = _converter.mapXPixelToCoord(i);
                points.add(Point2D.Double(x, function!!(x)));
            }


            var current = points.first();
            points.forEach {

                drawLine(
                    _converter.mapXCoordToPixel(current.x),
                    _converter.mapYCoordToPixel(current.y),
                    _converter.mapXCoordToPixel(it.x),
                    _converter.mapYCoordToPixel(it.y)
                );

                current = it;
            };
        }
    }

    fun Clear() {
        this.function = null;
    }

    override var name: String = "FunctionPainter";

    override var isVisible: Boolean = true;


}