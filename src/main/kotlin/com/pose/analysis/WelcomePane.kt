package com.pose.analysis

import com.pose.analysis.App.Companion.largeFont
import com.pose.analysis.App.Companion.mediumFont
import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.MainPane.screenHeight
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

object WelcomePane: StackPane() {

    // Sets up the 4 lines of the main welcome text
    private val welcomeText = Label("Welcome to PoseAnalysis!")
    private val helpText = Label("For a tutorial, click Help -> Intro")
    private val startText = Label("To get started, click File -> Open, then choose Video or Animation")
    private val infoText = Label("For more information, click Help -> ReadMe")

    init {

        // Makes sure the text is aligned to the top-center of the pane
        WelcomePane.alignment = Pos.TOP_CENTER

        // Sets the font, color, and position of the first line
        welcomeText.font = largeFont
        welcomeText.setColor(mutedTextColor)
        welcomeText.translateY = screenHeight * 0.125

        // Sets the font, color, and position of the second line
        helpText.font = mediumFont
        helpText.setColor(mutedTextColor)
        helpText.translateY = screenHeight * 0.5

        // Sets the font, color, and position of the third line
        startText.font = mediumFont
        startText.setColor(mutedTextColor)
        startText.translateY = screenHeight * 0.625

        // Sets the font, color, and position of the fourth line
        infoText.font = mediumFont
        infoText.setColor(mutedTextColor)
        infoText.translateY = screenHeight * 0.75

        // Adds every line to the pane
        WelcomePane.children.addAll(

            welcomeText,
            helpText,
            startText,
            infoText

        )

        // Makes sure that the pane is centered.
        // When the width of the pane changes, it re-centers itself
        WelcomePane.widthProperty().addListener { _, _, newWidth ->
            WelcomePane.layoutX = (MainPane.screenWidth / 2.0) - (newWidth.toDouble() / 2.0)
        }

        // Makes sure the pane is only visible when it needs to be
        WelcomePane.visibleProperty().bind(
            isWelcome
        )
    }
}