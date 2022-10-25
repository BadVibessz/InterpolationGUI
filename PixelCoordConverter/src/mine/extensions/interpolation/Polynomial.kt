package mine.polynomial

import extensions.eq
import extensions.neq
import kotlin.math.abs
import kotlin.text.StringBuilder


fun ReadPolynomial(input: String): Polynomial {

    val pattern = Regex("""([\+\-]?\d+\.?\d+[Ee]?[\+\-]?\d*)[x\^\d+]?|([\+\-]?\d+\.?\d*[Ee]?[\+\-]?\d*)x""");

    val matches = pattern.findAll(input);
    val coeffs = mutableListOf<Double>();

    matches.forEach { match ->
        val groups = match.groups
        val coeff = (if (groups[0] != null) groups[0] else groups[1]).toString();
        coeffs.add(coeff.toDouble());
    }
    return Polynomial(*coeffs.toDoubleArray());
}


open class Polynomial(vararg coeffs: Double) {
    private val _pows: String = "⁰¹²³⁴⁵⁶⁷⁸⁹"; // todo
    private val _coeff: MutableList<Double>;

    val coeff: List<Double>
        get() = _coeff.toList();

    val degree: Int
        get() = _coeff.size - 1;

    init {
        _coeff = coeffs.toMutableList();
        CorrectDegree();
    }

    // TODO: -=,+=,*=,/=, invoke, equals, Newton(на баллы) {метод добавления одного узла, замерить время лагранжа и ньютона}


    fun GetDerivative(): Polynomial {

        val list = mutableListOf<Double>();
        this._coeff.forEachIndexed { i, d ->
            if (i != 0)
                list.add(i * d)
        }
        return Polynomial(list);
    }

    constructor() : this(0.0)

    constructor(list: List<Double>) : this(*list.toDoubleArray()) {
    }

    constructor(copy: Polynomial) : this() {
        this._coeff.clear();
        copy._coeff.forEach { value -> this._coeff.add(value) };
    }

    private fun CorrectDegree() {
        while ((abs(_coeff.last()) eq 0.0) && _coeff.isNotEmpty() && _coeff.size != 1)
            _coeff.removeLast();
        if (_coeff.isEmpty()) _coeff.add(0.0);
    }

    private fun SplitIntoDigits(number: Int): MutableList<Int> {
        var i = number;
        val digits = mutableListOf<Int>();
        while (i > 0) {
            digits.add(i % 10);
            i /= 10;
        }
        return digits.asReversed();
    }

    override fun toString(): String {

        if (this._coeff.size == 1 && abs(this._coeff.last()) < 0.0000001) return "0.0";

        val builder = StringBuilder();

        _coeff.asReversed().forEachIndexed { index, value ->
            if (value neq 0.0) {
                val pow = _coeff.size - index - 1;
                if (value > 0 && pow != _coeff.size - 1) builder.append("+");
                if (value < 0) builder.append("-")
                if (value != 1.0 || pow == 0)
                    if (value % 1.0 == 0.0)
                        builder.append(abs(value).toInt())
                    else
                        builder.append((String.format("%.3f", abs(value))))

                if (pow != 0)
                    builder.append("x")
                if (pow >= 2) {
                    if (pow <= 9)
                        builder.append(_pows[pow]);
                    else {
                        val digits = SplitIntoDigits(pow);
                        val powBuilder = StringBuilder();
                        digits.forEach { d -> powBuilder.append(_pows[d]) }
                        builder.append(powBuilder);
                    }
                }

            }
        }
        return builder.toString()
    }

    operator fun invoke(x: Double): Double {
        var pow = 1.0;
        return _coeff.reduce { acc, d -> pow *= x; acc + d * pow }
    }

    override fun equals(other: Any?): Boolean =
        other is Polynomial && this.degree == other.degree && this._coeff == other._coeff;

    override fun hashCode(): Int = _coeff.hashCode();

    operator fun plus(other: Polynomial): Polynomial {
        val (min, max) = if (degree < other.degree) arrayOf(this, other) else arrayOf(other, this);
        val list = max._coeff.toDoubleArray();
        min._coeff.forEachIndexed { i, v -> list[i] += v }

        return Polynomial(*list);
    }

    operator fun plusAssign(other: Polynomial) {
//        val (min, max) = if (degree < other.degree) arrayOf(this, other) else arrayOf(other, this);
//        val list = max._coeff.toDoubleArray();
//        min._coeff.forEachIndexed { i, v -> list[i] += v }
//        this._coeff.clear();
//        list.forEach { value -> this._coeff.add(value) }
//        this.CorrectDegree();

        other._coeff.forEachIndexed { i, d -> if (i < _coeff.size) _coeff[i] += d else _coeff.add(d); }
    }

    operator fun minusAssign(other: Polynomial) {
        this + -other;
    };


    operator fun minus(other: Polynomial): Polynomial = this + (-other);

    operator fun unaryMinus(): Polynomial {
        var result = mutableListOf<Double>();
        this._coeff.forEach { value -> result.add(-value) }
        return Polynomial(*result.toDoubleArray());
    }

    operator fun times(coeff: Double): Polynomial = Polynomial(*DoubleArray(degree + 1) { _coeff[it] * coeff })

    operator fun timesAssign(coeff: Double) {
        val temp = this._coeff;
        this._coeff.clear();
        if (abs(coeff) eq 0.0)
            this._coeff.add(0.0);

        temp.forEach { value -> this._coeff.add(value * coeff) }
        this.CorrectDegree();
    }

    operator fun times(other: Polynomial): Polynomial {
        val temp = DoubleArray(degree + other.degree + 1) { 0.0 }
        _coeff.forEachIndexed { thisIndex, thisCoeff ->
            other._coeff.forEachIndexed { otherIndex, otherCoeff ->
                temp[thisIndex + otherIndex] += thisCoeff * otherCoeff
            }
        }
        return Polynomial(*temp);
    }

    operator fun timesAssign(other: Polynomial) {
        val temp = DoubleArray(degree + other.degree + 1) { 0.0 }
        _coeff.forEachIndexed { thisIndex, thisCoeff ->
            other._coeff.forEachIndexed { otherIndex, otherCoeff ->
                temp[thisIndex + otherIndex] += thisCoeff * otherCoeff
            }
        }

        this._coeff.clear();
        temp.forEach { value -> this._coeff.add(value); }
        this.CorrectDegree();

    }

    operator fun div(coeff: Double): Polynomial { // todo: maybe return null instead of exception's throw
        if (abs(coeff) neq 0.0)
            return this * (1.0 / coeff)
        else
            throw Exception("Null division");
    }


    operator fun div(other: Polynomial): Polynomial // todo?
    {

        if (abs(this._coeff.last()) eq 0.0)
            throw Exception("Старший член многочлена делимого не может быть 0")
        if (abs(other._coeff.last()) eq 0.0)
            throw Exception("Старший член многочлена делителя не может быть 0")
        return Polynomial();
    }
}