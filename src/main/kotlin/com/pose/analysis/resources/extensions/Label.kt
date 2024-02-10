package com.pose.analysis.resources.extensions

import javafx.scene.control.Label
import javafx.scene.paint.Color

// Sets the color of a label
fun Label.setColor(color: Color): Label {

    // Changes the text fill to the provided color
    this.textFill = color

    // Returns the modified label
    return this
}