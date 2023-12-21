package com.example.jfxbase

import javafx.scene.control.Label
import javafx.scene.layout.Pane

object MainPane: Pane() {
    val text = Label("Hi")

    init {
        MainPane.children.addAll(text)
    }
}