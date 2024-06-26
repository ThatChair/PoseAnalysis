package com.pose.analysis

import com.pose.analysis.App.Companion.VERSION
import com.pose.analysis.App.Companion.bgColor
import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.App.Companion.smallFont
import com.pose.analysis.App.Companion.textColor
import com.pose.analysis.resources.extensions.setColor
import com.pose.analysis.resources.functions.println
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.stage.Screen

// The main pane (I know, great name, right!) of the app. Everything is added to this pane
object MainPane : VBox() {

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

    val angularVelocityText = Label()

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

        // Sets up the version text
        val versionText = Label(VERSION)
        versionText.layoutX = (screenWidth - (versionText.text.length * 7))
        versionText.font = smallFont
        versionText.setColor(mutedTextColor)

        angularVelocityText.layoutY = 30.0
        angularVelocityText.font = smallFont
        angularVelocityText.setColor(mutedTextColor)

        // Adds all children to the top pane
        topPane.children.addAll(
            MainMenuBar,
            versionText,
            angularVelocityText
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
        bottomPane.prefHeight = SliderPane.SLIDER_HEIGHT
        bottomPane.maxHeight = SliderPane.SLIDER_HEIGHT * 2

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

        // Loads if multiple threads are going
        App.numLoadingThreads.addListener { _, _, newValue ->
            println("Threads changed to $newValue")
            if (newValue.toInt() <= 0) {
                stopLoading()
            } else if (!loadingGif.isVisible) {
                startLoading()
            }
        }

        animationName.addListener { _, _, newValue ->
            if (newValue != "") {
                App.stage.title = "Pose Analysis - $newValue"
            } else {
                App.stage.title = "Pose Analysis"
            }
        }


        MainPane.setOnKeyPressed { e ->
            if (animation.isNotEmpty()) {
                if (e.code === KeyCode.PERIOD) {
                    increaseFrame()
                } else if (e.code === KeyCode.COMMA) {
                    decreaseFrame()
                }
            }
        }

        MainMenuBar.versionNumShowing.addListener { _, _, newValue ->
            if (newValue) {
                versionText.text = VERSION
            } else {
                versionText.text = ""
            }
        }
    }

    // Shows the loading gif
    private fun startLoading() {
        println("Starting loading...")
        loadingGif.isVisible = true
    }

    // Hides the loading gif
    private fun stopLoading() {
        println("Stopping loading")
        loadingGif.isVisible = false
    }

    fun addUpdateText() {
        // Sets up the update text
        val updateText = Label("Update Available!")
        updateText.layoutX = (screenWidth - (updateText.text.length * 6.6))
        updateText.layoutY = 15.0
        updateText.font = smallFont
        updateText.setColor(textColor)

        topPane.children.add(updateText)

        MainPane.children[0] = topPane
    }
}