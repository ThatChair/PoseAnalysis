package com.pose.analysis

import com.pose.analysis.MainPane.screenWidth
import javafx.scene.layout.Pane

object `3DPane`: Pane() {
    init {
        `3DPane`.minWidth = screenWidth / 4
        `3DPane`.prefWidth = screenWidth / 2.0
        `3DPane`.maxWidth = screenWidth * 0.75
    }
}