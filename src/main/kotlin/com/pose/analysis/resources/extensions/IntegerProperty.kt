package com.pose.analysis.resources.extensions

import javafx.beans.property.IntegerProperty

fun IntegerProperty.increment(value: Int) {
    this.set(this.get() + value)
}

fun IntegerProperty.decrement(value: Int) {
    this.set(this.get() - value)
}