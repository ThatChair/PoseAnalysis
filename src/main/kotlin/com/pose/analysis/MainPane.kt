package com.pose.analysis

import com.pose.analysis.App.Companion.bgColor
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane

// The main pane (I know, great name right!) of the app. Everything is added to this pane
object MainPane: Pane() {

    init {
        // Sets the background to the specified background color in App
        MainPane.background = Background(BackgroundFill(bgColor, CornerRadii(0.0), Insets.EMPTY))

        // Adds all the children to the main pane
        MainPane.children.addAll(

            MainMenuBar

        )
    }
}