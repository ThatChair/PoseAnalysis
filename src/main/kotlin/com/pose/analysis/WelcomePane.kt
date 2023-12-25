package com.pose.analysis

import com.pose.analysis.App.Companion.largeFont
import com.pose.analysis.App.Companion.mediumFont
import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.MainPane.screenHeight
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

object WelcomePane: StackPane() {

    val welcomeText = Label("Welcome to PoseAnalysis!")

    val helpText = Label("For a tutorial, click help -> intro")

    val startText = Label("To get started, click file -> open, then choose video, pose, or animation")

    val infoText = Label("For more information, click help -> readme")

    init {


        WelcomePane.alignment = Pos.TOP_CENTER

        welcomeText.font = largeFont
        welcomeText.setColor(mutedTextColor)
        welcomeText.translateY = screenHeight * 0.125

        helpText.font = mediumFont
        helpText.setColor(mutedTextColor)
        helpText.translateY = screenHeight * 0.5

        startText.font = mediumFont
        startText.setColor(mutedTextColor)
        startText.translateY = screenHeight * 0.625

        infoText.font = mediumFont
        infoText.setColor(mutedTextColor)
        infoText.translateY = screenHeight * 0.75

        WelcomePane.children.addAll(

            welcomeText,
            helpText,
            startText,
            infoText
        )

        WelcomePane.widthProperty().addListener { _, _, newWidth ->
            WelcomePane.layoutX = (MainPane.screenWidth / 2.0) - (newWidth.toDouble() / 2.0)
        }

        WelcomePane.visibleProperty().bind(
            isWelcome
        )
    }
}