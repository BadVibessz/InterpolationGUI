package mine.extensions.painter

import java.awt.Graphics

interface Painter {

    var isVisible : Boolean;
    var name: String;

    fun paint(g: Graphics?)
}