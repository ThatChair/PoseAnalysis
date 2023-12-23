package com.pose.analysis.MenuBar


import com.pose.analysis.App.Companion.path
import com.pose.analysis.MainPane
import com.pose.analysis.addJson
import javafx.scene.control.MenuBar
import java.io.File

// The main menu bar that at the top of the screen
object MainMenuBar: MenuBar() {

    // Defines the path to the Json file used to make the menu bar
    val menuBarFile = File("${path}\\res\\MenuBar.json")

    init {
        // If on Mac, the is on the Mac, not the app
        MainMenuBar.isUseSystemMenuBar = true
        // Sets the background of the menu bar to the same as the main pane, so it blends in.
        MainMenuBar.background = MainPane.background
        // Calls the function to turn the Json file to a menu bar
        MainMenuBar.addJson(menuBarFile)
        // Debugging
        println(MainMenuBar.menus[0].items[0].graphic)
    }
}