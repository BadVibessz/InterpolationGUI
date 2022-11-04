package mine.extensions.painter

import mine.extensions.converter.PixelCoordConverter
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.lang.StringBuilder

object MouseLocationPainter : Painter {

    private val _converter = PixelCoordConverter;

    var MouseLocation = Point(0,0);
    override var isVisible: Boolean = true;

    override fun paint(g: Graphics?) {

        g?.apply {
            color = Color.LIGHT_GRAY;

            val dx = -17;
            val dy = 30;
            val gap = 12;

            val pixelX = MouseLocation.x;
            val pixelY = MouseLocation.y;

            val coordX = _converter.mapXPixelToCoord(pixelX);
            val coordY = _converter.mapYPixelToCoord(pixelY);

            val pixelStr = "($pixelX;$pixelY)";

            val builder = StringBuilder();

            builder.append("(");
            builder.append(String.format("%.1f",coordX));
            builder.append(";");
            builder.append(String.format("%.1f",coordY));
            builder.append(")");


            drawString(pixelStr, pixelX + dx, pixelY + dy);
            drawString(builder.toString(), pixelX + dx, pixelY + dy + gap);
        }
    }

    override var name: String = "MouseLocationPainter";
}