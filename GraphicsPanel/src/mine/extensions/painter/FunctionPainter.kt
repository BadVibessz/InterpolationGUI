package mine.extensions.painter

import mine.extensions.converter.PixelCoordConverter
import mine.polynomial.NewtonPolynomial
import java.awt.*
import java.awt.geom.Point2D
import kotlin.math.abs


object FunctionPainter : Painter {

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

    var newtonPolynomial: NewtonPolynomial? = null;

    private val _points = mutableListOf<Point2D>();

    fun AddPoint(point: Point) {

        val eps = (abs(_converter.xMax - _converter.xMin)) / 80;

        val convertedPoint = _converter.mapPixelToCoord(Point(point.x, point.y));

        if (this._points.find { p -> abs(p.x - convertedPoint.x) <= eps } == null) {
            this._points.add(convertedPoint);

            if (_points.size == 1)
                this.newtonPolynomial = NewtonPolynomial(_points.associateBy({ it.x }, { it.y }))
            else
                this.newtonPolynomial?.AddNode(convertedPoint.x, convertedPoint.y);

        }


    }

    fun RemovePoint(point: Point) {
        var eps = (abs(_converter.xMax - _converter.xMin)) / 60;

        if (eps < 0.1) eps = 0.05;

        val toRemove = this._points.find { p ->
            abs(p.x - _converter.mapXPixelToCoord(point.x)) <= eps && abs(p.y - _converter.mapYPixelToCoord(point.y)) <= eps
        }

        if (toRemove != null)
            this._points.remove(toRemove);

        if (_points.size != 0)
            this.newtonPolynomial = NewtonPolynomial(_points.associateBy({ it.x }, { it.y }))
        else this.newtonPolynomial = null;
    }


    private fun CalculateFunctionPoints(): List<Point2D> {

        newtonPolynomial?.apply {

            val precision = 100;

            val poly = GetPolynomial();

            val left = (_converter.xMin * precision).toInt();
            val right = (_converter.xMax * precision).toInt();

            val list = mutableListOf<Point2D>()
            for (i in left..right)
                list.add(Point2D.Double(i / precision.toDouble(), poly(i / precision.toDouble())))

            return list;
        }

        return emptyList();
    }

    override fun paint(g: Graphics?) {

        if(!isVisible) return;
        if (newtonPolynomial == null) return;

        g as Graphics2D?;
        g?.apply {

            setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            color = FunctionColor;
            setStroke(BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)) // todo: understand why fps drops

            val functionPoints = CalculateFunctionPoints();

            var current = functionPoints.first();
            functionPoints.forEach {

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

    override fun getName(): String = "FunctionPainter"

    override var isVisible: Boolean = true;


}