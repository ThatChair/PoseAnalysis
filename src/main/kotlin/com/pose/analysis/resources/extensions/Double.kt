package com.pose.analysis.resources.extensions

import kotlin.math.pow
import kotlin.math.roundToInt

// Remaps a double from the specified range to the other specified range
fun Double.remap(fromRangeStart: Double, fromRangeEnd: Double, toRangeStart: Double, toRangeEnd: Double): Double {
    // Gets the range of the fromRange
    val fromRange = fromRangeEnd - fromRangeStart

    // Gets the range of the toRange
    val toRange = toRangeEnd - toRangeStart

    // Normalizes the value
    val normalizedValue = (this - fromRangeStart) / fromRange

    // Returns the range mapped to the new range
    return toRangeStart + normalizedValue * toRange
}

fun Double.round(digits: Int): Double {
    if (!this.isNaN()) {
        val modulo = 10.0.pow(digits.toDouble())
        return (this * modulo).roundToInt() / modulo
    } else {
        return this
    }
}