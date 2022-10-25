package mine.extensions.painter

import java.awt.Graphics

interface Painter {
    var width : Int;
    var height : Int;
    var isVisible : Boolean;

    fun paint(g: Graphics?)
    fun getName(): String;
}