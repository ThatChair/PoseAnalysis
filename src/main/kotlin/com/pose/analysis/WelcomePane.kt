package com.pose.analysis

import com.pose.analysis.App.Companion.largeFont
import com.pose.analysis.App.Companion.mediumFont
import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.MainPane.screenHeight
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

object WelcomePane: StackPane() {

    private val welcomeText = Label("Welcome to PoseAnalysis!")

    private val helpText = Label("For a tutorial, click Help -> Intro")

    private val startText = Label("To get started, click File -> Open, then choose Video, Pose, or Animation")

    private val infoText = Label("For more information, click Help -> Readme")

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