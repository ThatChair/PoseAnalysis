package com.pose.analysis


import com.pose.analysis.App.Companion.textColor
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem

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
}