package com.pose.analysis.resources.extensions

import javafx.beans.property.IntegerProperty

// Increments the IntegerProperty by the given value
fun IntegerProperty.increment(value: Int) {
    this.set(this.get() + value)
}

// Decrements the IntegerProperty by the given value
fun IntegerProperty.decrement(value: Int) {
    this.set(this.get() - value)
}