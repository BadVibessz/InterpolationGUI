package extensions

import java.awt.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

infix fun Double.eq(other: Double) = abs(this - other) < max(Math.ulp(this), Math.ulp(other));
infix fun Double.neq(other: Double) = !(this eq other);
infix fun Double.leq(other: Double) = this < other || this eq other;
infix fun Double.req(other: Double) = this > other || this eq other;
infix fun Double.lt(other: Double) = this < other && this neq other;
infix fun Double.rt(other: Double) = this > other && this neq other;

