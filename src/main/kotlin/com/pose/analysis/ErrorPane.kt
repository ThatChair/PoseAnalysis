package com.pose.analysis

import com.pose.analysis.App.Companion.mediumFont
import com.pose.analysis.App.Companion.smallFont
import com.pose.analysis.ErrorPopup.showErrorPopup
import com.pose.analysis.MainPane.doneLoading
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

object ErrorPane : TextFlow() {

    // Defines the background of the error pane
    private val errorBackground = Background(BackgroundFill(App.mutedTextColor, CornerRadii(10.0), Insets.EMPTY))

    init {

        // Sets the background of the error pane
        ErrorPane.background = errorBackground

    }

    // Displays the error, called when an error is encountered
    fun showError(message: String, desc: String? = null) {

        doneLoading()

        // Prints the error
        println(if (desc != null) "ERROR: $message\n$desc" else "ERROR: $message")

        // puts the welcome message back on the screen
        isWelcome.set(true)

        // Removes all previous errors from the error pane
        ErrorPane.children.clear()

        // Sets the text for the error and the description
        val text = Label("ERROR: $message")
        val smallText = Label(desc)

        // Sets the color for the error and the description
        text.setColor(Color.CRIMSON)
        smallText.setColor(Color.CRIMSON)

        // Sets the font for the error and the description
        text.font = mediumFont
        smallText.font = smallFont

        // Adds the error and the description to the pane
        ErrorPane.children.addAll(text, Text("\n"), smallText)
        // Adds padding to the pane so the text isn't right at the edge
        ErrorPane.padding = Insets(20.0)

        // Calls the function to display the popup
        showErrorPopup()
    }
}