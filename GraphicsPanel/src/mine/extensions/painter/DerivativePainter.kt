package mine.extensions.painter

import mine.extensions.converter.PixelCoordConverter
import java.awt.Graphics

object DerivativePainter : Painter{

    private val _converter = PixelCoordConverter;

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

        //FunctionPainter.paint(g);
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

}