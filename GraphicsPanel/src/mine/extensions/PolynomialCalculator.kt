package mine.extensions

import mine.extensions.converter.PixelCoordConverter
import mine.extensions.painter.FunctionPainter
import mine.polynomial.NewtonPolynomial
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.abs

object PolynomialCalculator {

    private val _converter = PixelCoordConverter;
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


    fun CalculateFunctionPoints(): List<Point2D> {

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

    fun CalculateDerivativePoint(): List<Point2D>
    {
        newtonPolynomial?.apply {

            val precision = 100;

            val poly = GetPolynomial().GetDerivative();

            val left = (_converter.xMin * precision).toInt();
            val right = (_converter.xMax * precision).toInt();

            val list = mutableListOf<Point2D>()
            for (i in left..right)
                list.add(Point2D.Double(i / precision.toDouble(), poly(i / precision.toDouble())))

            return list;
        }

        return emptyList();
    }

    fun Clear()
    {
        this._points.clear();
        this.newtonPolynomial = null;
    }
}