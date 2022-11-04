package mine.extensions

import mine.extensions.painter.Painter
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel

class GraphicsPanel() : JPanel() {

    private val _painters: MutableList<Painter> = mutableListOf()

    fun addPainter(painter: Painter) = _painters.add(painter);
    fun removePainter(painter: Painter) = _painters.remove(painter);
    fun addPainters(painters: List<Painter>) = this._painters.addAll(painters);
    fun removePainters(painters: List<Painter>) = this._painters.removeAll(painters);

    fun getPainter(painterName: String): Painter? =
        this._painters.find {it.name == painterName };


    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        g?.apply {

            g as Graphics2D;

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            _painters.forEach { it.paint(g) } // TODO: if(it.visible) it.paint(g)
        }
    }
}