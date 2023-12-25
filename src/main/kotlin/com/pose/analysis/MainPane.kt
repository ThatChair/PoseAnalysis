package com.pose.analysis

import com.pose.analysis.App.Companion.bgColor
import javafx.geometry.Insets
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.stage.Screen

// The main pane (I know, great name, right!) of the app. Everything is added to this pane
object MainPane: Pane() {

    // Sets up the loading gif from the file
    private val loadingGif = ImageView("loading.gif")

    init {
        // Stores the primary screen properties for later use
        val screen = Screen.getPrimary()

        // Stores the primary screen width for later use
        val width = screen.visualBounds.width
        // Stores the primary screen height for later use
        val height = screen.visualBounds.height


        // Sets the background to the specified background color in App
        MainPane.background = Background(BackgroundFill(bgColor, CornerRadii(0.0), Insets.EMPTY))

        // Moves the loading gif to the center of the screen
        loadingGif.layoutX = (width / 2.0) - (loadingGif.image.width / 2.0)
        loadingGif.layoutY = (height / 2.0) - (loadingGif.image.height / 2.0)
        // Hides loading gif
        loadingGif.isVisible = false

        // Adds all the children to the main pane
        MainPane.children.addAll(

            loadingGif,
            MainMenuBar

        )

    }

    // Shows the loading gif
    fun startLoading() {
        println("Starting loading...")
        loadingGif.isVisible = true
    }

    // Hides the loading gif
    fun doneLoading() {
        println("Stopping loading")
        loadingGif.isVisible = false
    }
}