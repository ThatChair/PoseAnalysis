package com.pose.analysis

import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.GaussianBlur
import javafx.stage.Popup


object ErrorPopup : Popup() {

    init {
        // Adds the error pane to the popup
        ErrorPopup.content.addAll(
            ErrorPane
        )

        // Adds the blur to main pane when the error popup is showing
        ErrorPopup.setOnShowing {

            // Sets up the adjustment
            val adjustment = ColorAdjust()
            // Sets up the blur
            val blur = GaussianBlur(10.0)
            // Adds the blur to the adjustment
            adjustment.input = blur
            // Applies the adjustment to the MainPane
            MainPane.effect = adjustment

        }
        ErrorPopup.setOnHiding {

            // Sets up the adjustment that overwrites the current adjustment
            val adjustment = ColorAdjust()
            // Makes a blur with no blur
            val blur = GaussianBlur(0.0)
            // Adds the blur to the adjustment
            adjustment.input = blur
            // Overwrites the current adjustment
            MainPane.effect = adjustment
        }

        // When the mouse is clicked, hide the popup
        MainPane.setOnMouseClicked {
            if (ErrorPopup.isShowing) {
                hideErrorPopup()
            }
        }
    }

    // Shows the error popup
    fun showErrorPopup() {
        ErrorPopup.show(App.stage)
    }

    // Hides the error popup. Amazing
    private fun hideErrorPopup() {
        ErrorPopup.hide()
    }
}