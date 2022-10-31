package mine.extensions.painter

import mine.extensions.converter.PixelCoordConverter
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.abs

object PointPainter : Painter {

    private val _converter = PixelCoordConverter;
    private val _points = mutableListOf<Point2D>();
    private val _radius = 7;

    var PointColor = Color.WHITE;

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


    fun AddPoint(point: Point) {
        //this._points.add(_converter.mapPixelToCoord(point));

        val eps = (abs(_converter.xMax - _converter.xMin)) / 80;

        if (this._points.find { p -> abs(p.x - _converter.mapXPixelToCoord(point.x)) <= eps } == null)
            this._points.add(_converter.mapPixelToCoord(point));
        //this._points.add(point);
    }

    fun RemovePoint(point: Point) {
        var eps = (abs(_converter.xMax - _converter.xMin)) / 60;

        if (eps < 0.1) eps = 0.05;

        val toRemove = this._points.find { p ->
            abs(p.x - _converter.mapXPixelToCoord(point.x)) <= eps && abs(p.y - _converter.mapYPixelToCoord(point.y)) <= eps
        }

        if (toRemove != null)
            this._points.remove(toRemove);
    }

    override fun paint(g: Graphics?) {

        if(!isVisible) return;

        g as Graphics2D?;
        g?.apply {

            color = PointColor;

            _points.forEach {
                fillOval(
                    _converter.mapXCoordToPixel(it.x) - _radius / 2,
                    _converter.mapYCoordToPixel(it.y) - _radius / 2,
                    _radius, _radius
                )
            };
        }
    }

    override fun getName(): String = "PointPainter";

    fun Clear()
    {
        this._points.clear();
    }

    init {
        //todo: do we need to reasign value?
//        _converter.width = width;
//        _converter.height = height;


    }

}