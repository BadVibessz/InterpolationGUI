package mine.extensions.painter

import mine.extensions.PolynomialCalculator
import mine.extensions.converter.PixelCoordConverter
import java.awt.*
import java.awt.geom.Point2D


class FunctionPainter : Painter {

    private val _converter = PixelCoordConverter;

    var FunctionColor : Color = Color.BLACK;

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

     var Points = mutableListOf<Point2D>();

    override fun paint(g: Graphics?) {

        if(Points.size == 0) return;
        if(!isVisible) return;


        g as Graphics2D?;
        g?.apply {

            setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            color = FunctionColor;
            setStroke(BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)) // todo: understand why fps drops

            var current = Points.first();
            Points.forEach {

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

    fun Clear()
    {
        this.Points.clear();
    }

    override fun getName(): String = "FunctionPainter"

    override var isVisible: Boolean = true;


}