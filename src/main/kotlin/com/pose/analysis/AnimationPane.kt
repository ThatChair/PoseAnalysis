package com.pose.analysis

import com.pose.analysis.MainPane.screenHeight
import com.pose.analysis.MainPane.screenWidth
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.HBox
import javafx.stage.Screen

object AnimationPane: HBox() {
    init {

        AnimationPane.height = screenHeight - SliderPane.height - MainMenuBar.height - 20.0
        AnimationPane.width = screenWidth

        AnimationPane.children.addAll(
            VideoPane,
             `3DPane`
        )

        AnimationPane.visibleProperty().bind(
            Bindings.
            `when`(
                isWelcome.not()
                    .and(MainPane.loadingGif.visibleProperty().not())
            )
                .then(true)
                .otherwise(false)
        )
    }
}