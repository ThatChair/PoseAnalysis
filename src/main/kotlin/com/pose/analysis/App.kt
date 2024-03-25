package com.pose.analysis

import com.pose.analysis.ErrorPane.showError
import com.pose.analysis.resources.extensions.decrement
import com.pose.analysis.resources.extensions.increment
import com.pose.analysis.resources.functions.getCommand
import com.pose.analysis.resources.functions.getLatestRelease
import com.pose.analysis.resources.functions.println
import com.pose.analysis.resources.functions.runCommand
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Screen
import javafx.stage.Stage
import kotlin.concurrent.thread

// The App class, which sets up the entire application
class App : Application() {

    companion object {

        // Defines the version of the application
        const val VERSION = "v1.0.5"

        // Defines the version as an Int for version comparison
        const val VERSIONNUM = 105

        // Determines whether the app needs an update
        var needsUpdate = false

        // Determines if the app is running from intellij or a JAR
        private val isRunningFromJar: Boolean
            get() {
                // Gets the class name
                val className = App::class.java.name.replace(".", "/") + ".class"

                // Gets the classpath
                val classPath = App::class.java.classLoader.getResource(className)?.toString() ?: return false

                // Returns whether the app is running from intellij or a JAR
                return classPath.startsWith("jar:")
            }


        // Keeps track of the number of threads running
        var numLoadingThreads: IntegerProperty = SimpleIntegerProperty()

        // Sets up the stage, the top level container that contains the entire application
        lateinit var stage: Stage
        // Sets up a static BG color value for global use. From a scale of 0 (Black) to 255 (White)
        private const val BACKGROUNDVALUE = 35.0
        // Turns the static color value into a greyscale color usable by JavaFX
        val bgColor: Color = Color.color(BACKGROUNDVALUE / 255, BACKGROUNDVALUE / 255, BACKGROUNDVALUE / 255)
        // The opposite of the background color, for use in text or other nodes that need to be high visibility.
        val textColor: Color = Color.color((255 - BACKGROUNDVALUE) / 255, (255 - BACKGROUNDVALUE) / 255, (255 - BACKGROUNDVALUE) / 255)

        // A more muted version of textColor, easy on eyes, good for large text
        val mutedTextColor: Color = Color.color(((255 - BACKGROUNDVALUE) / 255) * 0.5, ((255 - BACKGROUNDVALUE) / 255) * 0.5, ((255 - BACKGROUNDVALUE) / 255) * 0.5)

        // Gets the path the application is located, used for finding assets
        val path: String = System.getProperty("user.dir")

        // Defines the path to the python dir that contains the backend and requirements.txt
        val pythonDir =
            if (isRunningFromJar) "$path\\python" else "$path\\src\\main\\python"

        var pythonPath: String = "python3.11"

        // Load the TTF file
        val smallFont: Font = Font.loadFont(App::class.java.getResourceAsStream("/VarelaRound-Regular.ttf"), 13.0)
        // Load the TTF file
        val mediumFont: Font = Font.loadFont(App::class.java.getResourceAsStream("/VarelaRound-Regular.ttf"), 36.0)
        // Load the TTF file
        val largeFont: Font = Font.loadFont(App::class.java.getResourceAsStream("/VarelaRound-Regular.ttf"), 100.0)

        var logString: String = ""

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
        stage.icons.add(Image("icon.png"))

        // Defines the bounds of the screen
        val bounds = Rectangle2D(
            screen.visualBounds.minX,
            screen.visualBounds.minY,
            screen.visualBounds.width,
            screen.visualBounds.height
        )
        // Sets the bounds of the scene to the screen bounds
        stage.scene = Scene(MainPane, bounds.width, bounds.height)
        // Rescales the stage to the scene
        stage.sizeToScene()
        // Shows the stage
        stage.show()

        // Loads libraries and checks for newer version
        isWelcome.set(false)
        thread {
            numLoadingThreads.increment(1)

            thread {
                numLoadingThreads.increment(1)

                // Checks latest release and if there is a newer version
                if (getLatestRelease() > VERSIONNUM) {
                    needsUpdate = true
                }

                numLoadingThreads.decrement(1)

            }

            try {

                // Gets the path to python3.11
                pythonPath = getCommand("where python3.11")

                println("Python3.11 found at: $pythonPath")

                // Updates pip
                runCommand("$pythonPath -m pip install --upgrade pip")

                // Installs requirements
                runCommand("$pythonPath -m pip install -r $pythonDir\\requirements.txt")
            } catch (e: Exception) {
                Platform.runLater {
                    showError(e.toString())
                }
            }

            numLoadingThreads.decrement(1)

            // Shows the welcome stuff
            isWelcome.set(true)

            if (needsUpdate) {
                Platform.runLater {
                    MainPane.addUpdateText()
                }
            }
        }
    }

    // Called when application is stopped
    override fun stop() {
        // Just a fun little notification when the application is stopped
        println("Bye")
    }
}