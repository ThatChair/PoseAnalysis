package com.pose.analysis

import javafx.application.Application
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage

// The App class, which sets up the entire application
class App : Application() {

    companion object {
        // Sets up the stage, the top level container that contains the entire application
        lateinit var stage: Stage
        // Sets up a static BG color value for global use. From a scale of 0 (Black) to 255 (White)
        val bgColorValue = 35.0
        // Turns the static color value into a greyscale color useable by JavaFX
        val bgColor = Color.color(bgColorValue / 255, bgColorValue / 255, bgColorValue / 255)
        // The opposite of the background color, for use in text or other nodes that need to be high visibility.
        val textColor = Color.color((255 - bgColorValue) / 255, (255 - bgColorValue) / 255, (255 - bgColorValue) / 255)

        // Gets the path the application is located, used for finding assets
        val path = System.getProperty("user.dir")

        @JvmStatic
        // Called upon startup of the application
        fun main(args: Array<String>) {
            // Launches the application
            launch(App::class.java, *args)
        }
    }
    // Called when the application is ready to launch
    override fun start(stage: Stage) {
        // Gets the properties of the primary screen
        val screen = Screen.getPrimary()

        // Sets the title of the app
        stage.title = "Pose Analysis"
        // Sets the stage of the app to the already created stage
        Companion.stage = stage
        // Sets the stage icon to the icon at the specified path
        stage.icons.add(Image("$path\\res\\icon.png"))

        // Defines the bounds of the screen
        val bounds = Rectangle2D(screen.visualBounds.minX, screen.visualBounds.minY, screen.visualBounds.width, screen.visualBounds.height)
        // Sets the bounds of the scene to the screen bounds
        stage.scene = Scene(MainPane, bounds.width, bounds.height)
        // Rescales the stage to the scene
        stage.sizeToScene()
        // Shows the stage
        stage.show()
    }

    // Called when application is stopped
    override fun stop() {
        // Just a fun little notification when the application is stopped
        println("Bye")
    }
}