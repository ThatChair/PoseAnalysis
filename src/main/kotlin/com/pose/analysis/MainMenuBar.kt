package com.pose.analysis


import com.pose.analysis.App.Companion.path
import com.pose.analysis.App.Companion.stage
import com.pose.analysis.App.Companion.textColor
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

// The main menu bar that at the top of the screen
object MainMenuBar: MenuBar() {

    // Sets up file menu and all submenus/graphics
    private val fileMenu = Menu("")
    private val fileMenuGraphic = Label("File")
    private val fileOpenMenu = Menu("Open")
    private val fileOpenVideoMenuItem = MenuItem("Video")
    private val fileOpenPoseMenuItem = MenuItem("Pose")
    private val fileOpenAnimationMenuItem = MenuItem("Animation")
    private val fileSettingsMenuItem = MenuItem("Settings")
    private val fileExportMenu = Menu("Export")
    private val fileExportVideoMenuItem = MenuItem("Video")
    private val fileExportAnimationMenuItem = MenuItem("Animation")

    // Sets up view menu and all submenus/graphics
    private val viewMenu = Menu("")
    private val viewMenuGraphic = Label("Menu")
    private val viewVideoMenuItem = MenuItem("Video")
    private val view3DPoseMenuItem = MenuItem("3D Pose")
    private val viewSplitMenuItem = MenuItem("Split")

    // Sets up help menu and all submenus/graphics
    private val helpMenu = Menu("")
    private val helpMenuGraphic = Label("Help")
    private val helpSearchMenuItem = MenuItem("Search")

    init {
        // If on Mac, the is on the Mac, not the app
        MainMenuBar.isUseSystemMenuBar = true
        // Sets the background of the menu bar to the same as the main pane, so it blends in.
        MainMenuBar.background = MainPane.background

        fileOpenVideoMenuItem.setOnAction {
            fileOpenVideoMenuItemFun()
        }

        // Adds all submenus to the File-Open Menu
        fileOpenMenu.items.addAll(

            fileOpenVideoMenuItem,
            fileOpenPoseMenuItem,
            fileOpenAnimationMenuItem

        )

        // Adds all submenus to the File-Export Menu
        fileExportMenu.items.addAll(

            fileExportVideoMenuItem,
            fileExportAnimationMenuItem

        )

        // Sets the textcolor of the File Menu graphic to the correct color
        fileMenuGraphic.setColor(textColor)
        // Sets the graphic of the File Menu
        fileMenu.graphic = fileMenuGraphic

        // Adds all submenus to the File Menu
        fileMenu.items.addAll(

            fileOpenMenu,
            fileSettingsMenuItem,
            fileExportMenu

        )

        // Sets the color of the View Menu graphic to the correct color
        viewMenuGraphic.setColor(textColor)
        // Sets the graphic of the View Menu
        viewMenu.graphic = viewMenuGraphic

        // Adds all submenus to the View Menu
        viewMenu.items.addAll(
            viewVideoMenuItem,
            view3DPoseMenuItem,
            viewSplitMenuItem
        )

        // Sets the color of the Help Menu graphic to the correct color
        helpMenuGraphic.setColor(textColor)
        // Sets the graphic of the Help Menu
        helpMenu.graphic = helpMenuGraphic

        // Adds all submenus to the Help Menu
        helpMenu.items.addAll(

            helpSearchMenuItem

        )

        // Adds all submenus to the Main Menu Bar
        MainMenuBar.menus.addAll(

            fileMenu,
            viewMenu,
            helpMenu

        )
    }

    // Called when the File-Open-Video MenuItem is clicked. Opens the selected file with main.py.
    private fun fileOpenVideoMenuItemFun() {

        // Sets up the file chooser
        val fileChooser = FileChooser()

        // Sets the title of the file chooser
        fileChooser.title = "Pick a video to analyze"

        // Makes a filter that only accepts MP4 files
        val extensionFilter = ExtensionFilter("MP4 files (*.mp4)", "*.mp4")
        // Adds the filter to the file chooser
        fileChooser.extensionFilters.add(extensionFilter)

        // Opens the file chooser
        val selectedFile: File? = fileChooser.showOpenDialog(stage)

        // Checks if there is a selected file
        if (selectedFile != null) {

            // Sets the destination and desired file name for copying
            val destinationDirectory = File("$path\\res\\temp" )
            val newFileName = "vid.mp4"

            try {
                // Sets the destination path with the destination directory and new file name
                val destinationPath: Path = destinationDirectory.toPath().resolve(newFileName)

                // Copies the file to the destination
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING)
                println("File copied and renamed to: ${destinationPath.toAbsolutePath()}")

            } catch (e: Exception) {

                // Prints an error if an error occurs
                e.printStackTrace()
                println("Error copying the file.")

            }
        } else {

            // Prints if no file is selected. Duh.
            println("No file selected")

        }

        // Defines the path to the python script that analyzes the copied file
        val pythonScript = if (isRunningFromJar()) "$path\\python\\main.py" else "$path\\src\\main\\python\\main.py"

        try {

            // Creates and starts the process to run the python script with a command
            val processBuilder = ProcessBuilder(listOf("python", pythonScript, path))
            val process = processBuilder.start()

            // Waits for the process to finish
            val exitCode = process.waitFor()

            // Read the output of the process
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }

            // Capture and print standard error
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            var errorLine: String?
            while (errorReader.readLine().also { errorLine = it } != null) {
                System.err.println(errorLine)
            }

            // Print the exit code
            println("Exit Code: $exitCode")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}