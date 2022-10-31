package mine.extensions.painter

import mine.extensions.PolynomialCalculator
import mine.extensions.converter.PixelCoordConverter
import java.awt.Color
import java.awt.Graphics

object DerivativePainter : Painter {

    private val _converter = PixelCoordConverter;
    private val _functionPainter = FunctionPainter();

    var DerivativeColor: Color = Color.BLACK;


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

    override fun paint(g: Graphics?) {

        if(!isVisible) return;

        val points = PolynomialCalculator.CalculateDerivativePoint();
        if (points.size == 0) return;

        _functionPainter.Points = points.toMutableList();
        _functionPainter.FunctionColor = DerivativeColor;
        _functionPainter.paint(g);
    }

    override fun getName(): String = "DerivativePainter";

    fun Clear()
    {
        _functionPainter.Clear();

    }
}