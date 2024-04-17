package com.pose.analysis


import com.pose.analysis.App.Companion.path
import com.pose.analysis.App.Companion.pythonDir
import com.pose.analysis.App.Companion.pythonPath
import com.pose.analysis.App.Companion.smallFont
import com.pose.analysis.App.Companion.stage
import com.pose.analysis.App.Companion.textColor
import com.pose.analysis.ErrorPane.showError
import com.pose.analysis.Pane3D.renderPerson
import com.pose.analysis.resources.extensions.decrement
import com.pose.analysis.resources.extensions.increment
import com.pose.analysis.resources.extensions.setColor
import com.pose.analysis.resources.functions.println
import com.pose.analysis.resources.functions.runCommand
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.concurrent.thread

// The main menu bar that at the top of the screen
object MainMenuBar : MenuBar() {

    // Sets up file menu and all submenus/graphics
    private val fileMenu = Menu("")
    private val fileMenuGraphic = Label("File")
    private val fileOpenMenu = Menu("Open")
    private val fileOpenVideoMenuItem = MenuItem("Video")
    private val fileOpenAnimationMenuItem = MenuItem("Animation")
    private val fileExportMenuItem = MenuItem("Export")

    // Sets up view menu and all submenus/graphics/
    private val viewMenu = Menu("")
    private val viewMenuGraphic = Label("View")
    private val viewVersionMenuItem = MenuItem("Version Number •")
    var versionNumShowing: BooleanProperty = SimpleBooleanProperty(true)
    private val viewAngularVelocityMenuItem = MenuItem("Angular Velocity (BETA)  ")
    val viewingAngularVelocity: BooleanProperty = SimpleBooleanProperty(false)

    // Sets up help menu and all submenus/graphics
    private val helpMenu = Menu("")
    private val helpMenuGraphic = Label("Help")
    private val helpInfoMenuItem = MenuItem("Intro")
    private val helpReadMeMenuItem = MenuItem("ReadMe")
//    private val helpSearchMenuItem = MenuItem("Search")

    init {
        // If on Mac, the is on the Mac, not the app
        MainMenuBar.isUseSystemMenuBar = true
        // Sets the background of the menu bar to the same as the main pane, so it blends in.
        MainMenuBar.background = MainPane.background

        fileOpenVideoMenuItem.setOnAction {
            loadVideo()
        }

        fileOpenAnimationMenuItem.setOnAction {
            importAnimation()
        }

        fileExportMenuItem.setOnAction {
            exportAnimation()
        }

        // Adds all submenus to the File-Open Menu
        fileOpenMenu.items.addAll(

            fileOpenVideoMenuItem,
            fileOpenAnimationMenuItem

        )

        // Sets the color of the View Menu graphic to the correct color
        fileMenuGraphic.setColor(textColor)
        // Sets the font of the View Meny graphic
        fileMenuGraphic.font = smallFont
        // Sets the graphic of the View Menu
        fileMenu.graphic = fileMenuGraphic

        // Adds all submenus to the File Menu
        fileMenu.items.addAll(

            fileOpenMenu,
            fileExportMenuItem,

            )

        // Sets the color of the View Menu graphic to the correct color
        viewMenuGraphic.setColor(textColor)
        // Sets the font of the Help Menu graphic
        viewMenuGraphic.font = smallFont
        // Sets the graphic of the Help Menu
        viewMenu.graphic = viewMenuGraphic

        viewVersionMenuItem.setOnAction {
            toggleVersionNum()
        }

        viewAngularVelocityMenuItem.setOnAction {
            toggleVelocity()
        }

        // Adds all submenus to the view menu
        viewMenu.items.addAll(

            viewVersionMenuItem,
            viewAngularVelocityMenuItem

        )

        // Sets the color of the Help Menu graphic to the correct color
        helpMenuGraphic.setColor(textColor)
        // Sets the font of the Help Menu graphic
        helpMenuGraphic.font = smallFont
        // Sets the graphic of the Help Menu
        helpMenu.graphic = helpMenuGraphic

        // Sets the action of both the helpInfoMenuItem and helpReadMeMenuItem
        helpInfoMenuItem.setOnAction {
            openMD("INFO.md")
        }
        helpReadMeMenuItem.setOnAction {
            openMD("README.md")
        }

        // Adds all submenus to the Help Menu
        helpMenu.items.addAll(

            helpInfoMenuItem,
            helpReadMeMenuItem
//            helpSearchMenuItem

        )

        // Adds all submenus to the Main Menu Bar
        MainMenuBar.menus.addAll(

            fileMenu,
            viewMenu,
            helpMenu

        )
    }

    // Called when the File-Open-Video MenuItem is clicked. Opens the selected file with main.py.
    private fun loadVideo() {

        println("Loading video")

        // Sets up the file chooser
        val fileChooser = FileChooser()

        // Sets the title of the file chooser
        fileChooser.title = "Load Video"

        // Makes a filter that only accepts MP4 files
        val extensionFilter = ExtensionFilter("Video files (*.mp4, *.mov)", "*.mp4", "*.mov")
        // Adds the filter to the file chooser
        fileChooser.extensionFilters.add(extensionFilter)

        // Opens the file chooser
        val selectedFile: File? = fileChooser.showOpenDialog(stage)

        if (selectedFile?.path?.contains(" ") == true) {
            Platform.runLater {
                showError("File or path cannot contain a space!")
            }
            return
        }

        // Starts a separate thread so the application doesn't freeze
        thread {

            App.numLoadingThreads.increment(1)

            // Checks if there is a selected file

            println("Successfully selected file")

            // Calls the start loading function on the original thread
            Platform.runLater {
                // Turns off welcome stuff if it's on
                isWelcome.set(false)
            }

            try {

                println("Running python from: $pythonDir\\main.py")

                if (selectedFile != null) {
                    runCommand("$pythonPath $pythonDir\\main.py $path ${selectedFile.path}")
                }

            } catch (e: Exception) {
                Platform.runLater {
                    showError("Cannot run python script", (e.message ?: "") + (e.cause?.message ?: ""))
                }
            }

            loadAnimation("./res/temp/data.json")

            // Calls the done loading function on the original thread
            Platform.runLater {
                animPercent.set(0.0)
                renderPerson()
            }

            App.numLoadingThreads.decrement(1)
        }
    }

    // Opens a file chooser and loads an animation from a json file
    private fun importAnimation() {

        println("Loading animation")

        // Sets up the file chooser
        val fileChooser = FileChooser()

        // Sets the title of the file chooser
        fileChooser.title = "Import Animation"

        // Makes a filter that only accepts MP4 files
        val extensionFilter = ExtensionFilter("JSON files (*.json)", "*.json")
        // Adds the filter to the file chooser
        fileChooser.extensionFilters.add(extensionFilter)

        // Opens the file chooser
        val selectedFile: File? = fileChooser.showOpenDialog(stage)

        if (selectedFile != null) {

            println("Successfully selected animation")

            // Turns off welcome stuff if it's on
            thread {
                App.numLoadingThreads.increment(1)

                Platform.runLater {
                    isWelcome.set(false)
                }
                loadAnimation(selectedFile.path)
                Platform.runLater {
                    renderPerson()
                }
                App.numLoadingThreads.decrement(1)
            }
        }


    }

    private fun exportAnimation() {

        println("Exporting animation")

        val fileChooser = FileChooser()
        fileChooser.title = "Export Animation"

        // Set initial directory and file name
        val initialFile = File("./res/temp/data.json")
        fileChooser.initialFileName = initialFile.name
        fileChooser.initialDirectory = initialFile.parentFile

        // Set extension filter if needed
        val jsonExtensionFilter = ExtensionFilter("JSON files (*.json)", "*.json")
        fileChooser.extensionFilters.add(jsonExtensionFilter)

        // Show save dialog
        val selectedFile = fileChooser.showSaveDialog(null)

        // If the user selected a file, export the data
        if (selectedFile != null) {
            exportData(selectedFile)
        }
    }

    private fun exportData(file: File) {

        println("Exporting data to $file")

        val sourcePath: Path = File("./res/temp/data.json").toPath()

        try {
            Files.copy(sourcePath, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            println("File copied successfully.")
        } catch (e: Exception) {
            showError("Error copying file: ${e.message}")
        }
    }

    private fun toggleVersionNum() {
        versionNumShowing.set(!versionNumShowing.get())
        if (versionNumShowing.get()) {
            viewVersionMenuItem.text = "Version Number •"
        } else {
            viewVersionMenuItem.text = "Version Number  "
        }
    }

    private fun toggleVelocity() {
        viewingAngularVelocity.set(!viewingAngularVelocity.get())
        if (viewingAngularVelocity.get()) {
            viewAngularVelocityMenuItem.text = "Angular Velocity (BETA) •"
        } else {
            viewAngularVelocityMenuItem.text = "Angular Velocity (BETA)  "
        }
    }

    private fun openMD(name: String) {

        println("Opening MD file at $name")

        try {
            val file = File("$path//$name")

            if (file.exists()) {
                Desktop.getDesktop().open(file)
            } else {
                showError("File not found: $path$name")
            }
        } catch (e: IOException) {
            showError("Error opening file: $e")
        }
    }
}