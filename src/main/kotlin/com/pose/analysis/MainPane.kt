package com.pose.analysis

import com.pose.analysis.App.Companion.bgColor
import javafx.geometry.Insets
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.stage.Screen

// The main pane (I know, great name, right!) of the app. Everything is added to this pane
object MainPane: VBox() {

    // Stores the primary screen properties for later use
    private val screen: Screen = Screen.getPrimary()

    // Stores the primary screen width for later use
    val screenWidth = screen.visualBounds.width
    // Stores the primary screen height for later use
    val screenHeight = screen.visualBounds.height

    private val topPane = Pane()
    val middlePane = Pane()
    private val bottomPane = Pane()

    // Sets up the loading gif from the file
    val loadingGif = ImageView("loading.gif")

    init {

        // Sets the background to the specified background color in App
        MainPane.background = Background(BackgroundFill(bgColor, CornerRadii(0.0), Insets.EMPTY))

        // Moves the loading gif to the center of the screen
        loadingGif.layoutX = (screenWidth / 2.0) - (loadingGif.image.width / 2.0)
        loadingGif.layoutY = (screenHeight / 2.0) - (loadingGif.image.height / 2.0)
        // Hides loading gif
        loadingGif.isVisible = false

        // Sets preferred and max height for the top pane
        topPane.prefHeight = 32.0
        topPane.maxHeight = screenHeight / 8.0

        // Adds all children to the top pane
        topPane.children.addAll(
            MainMenuBar
        )

        // Sets preferred and max height for the middle pane
        middlePane.prefHeight = screenHeight * 0.9
        middlePane.maxHeight = screenHeight * 0.9
        middlePane.prefWidth = screenWidth
        middlePane.maxWidth = screenWidth

        // Adds all children to the middle pane
        middlePane.children.addAll(
            loadingGif,
            WelcomePane,
            Pane3D
        )

        // Sets preferred and max height for the bottom pane
        bottomPane.prefHeight = SliderPane.SLIDERHEIGHT
        bottomPane.maxHeight = SliderPane.SLIDERHEIGHT * 2

        // Adds all children to the bottom pane
        bottomPane.children.addAll(
            SliderPane
        )

        // Adds all the children to the main pane
        MainPane.children.addAll(

            topPane,
            middlePane,
            bottomPane

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