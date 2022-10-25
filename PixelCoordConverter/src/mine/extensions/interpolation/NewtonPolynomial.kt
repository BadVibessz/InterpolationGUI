package mine.polynomial

class NewtonPolynomial(map: Map<Double, Double>) : Polynomial() {

    private val _polynomial: Polynomial
    private val _map: MutableMap<Double, Double>;
    fun GetPolynomial(): Polynomial = this._polynomial;

    init {
        val result = Polynomial();
        this._map = map.toMutableMap();

        var j = 0;

        repeat(map.size){
            result += CalculateBasePolynomial(j) * CalculateDividedDifferences(j + 1);
            j++;
        }
        _polynomial = result;
    }


    private fun CalculateBasePolynomial(j: Int): Polynomial {
        val result = Polynomial(1.0);
        val keys = _map.keys.take(j);

        var i = 0;
        while (i < j) {
            result *= Polynomial(-keys[i], 1.0);
            i++;
        }

        return result;
    }

    private fun CalculateDividedDifferences(n: Int): Double {  // TOODO: WRONG!!

        val keys = _map.keys.take(n).toList();
        val values = _map.values.take(n).toList();

        if (n == 1)
            return values.first();

        var i = 0;
        var result = 0.0;

        val production = fun(): Double {
            var temp = 1.0;
            var k = 0;
            while (k < n) {
                if (k != i)
                    temp *= keys[i] - keys[k];
                k++;
            }
            return temp;
        };

        while (i < n) {
            result += values[i] / production();
            i++;
        }
        return result;
    }

    fun AddNode(key: Double, value: Double) {
        this._map.put(key, value);
        val n = _map.size;

        this._polynomial += CalculateBasePolynomial(n - 1) *
                CalculateDividedDifferences(n);
    }

}