package mine.polynomial

import extensions.eq
import kotlin.math.abs

class LagrangePolynomial(hashMap: HashMap<Double, Double>) : Polynomial() {
    private val _polynomial: Polynomial

    init {

        val result = Polynomial();

        var j = 0;
        for ((key, value) in hashMap) {
            if (abs(value) eq 0.0) {
                j++;
                continue;
            }

            result += CalculateBasePolynomial(j, key, hashMap) * value;
            j++;
        }

        _polynomial = result;
    }

    fun GetPolynomial(): Polynomial = this._polynomial

    private fun CalculateBasePolynomial(j: Int, x_j: Double, hashMap: HashMap<Double, Double>): Polynomial {
        val result = Polynomial(1.0);

        var i = 0;
        for ((key) in hashMap) {
            if (j != i)
                result *= Polynomial(-key, 1.0) / (x_j - key);
            i++;
        }

        return result;
    }

}