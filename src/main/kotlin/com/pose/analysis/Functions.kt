package com.pose.analysis

import com.pose.analysis.App.Companion.textColor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.paint.Color
import java.io.File
import java.lang.reflect.Type

// Sets the color of a label
fun Label.setColor(color: Color): Label {

    // Changes the text fill to the provided color
    this.textFill = color

    // Returns the modified label
    return this
}

// Adds the contents of a json file to a menu bar
fun MenuBar.addJson(jsonFile: File, vararg functions: Pair<String, () -> Unit>) {

    // Sets up Gson
    val gson = Gson()

    // Defines the type the Json file needs to be converted to
    val type: Type = object : TypeToken<Map<String, Any>>() {}.type

    // Uses the addMap function to add the Json file converted to the type
    this.addMap(gson.fromJson(jsonFile.readText(), type))
}

// Adds the contents of a map to a menu bar
fun MenuBar.addMap(map: Map<String, Any>, vararg functions: Pair<String, () -> Unit>) {

    // Loops every item in the map
    for (item in map) {

        // Sets up a menu to be added to the MenuBar
        val menu = Menu()
        // Makes a graphic of text reading the key of the item in the map
        val menuGraphic = Label(item.key)
        // Sets the color of the text to the text color defined in the companion object of the app
        menuGraphic.setColor(textColor)
        // Sets the graphic of the menu to the predefined graphic
        menu.graphic = menuGraphic

        // Checks the datatype of the value of the item in the map
        when (item.value) {

            // If the value is a string, adds an item to the menu
            is String -> menu.items.add(MenuItem(item.value as String))

            // If the value is a list, adds the item to the menu using the add list function
            is List<*> -> menu.addList(item.value as List<Any>)

            // If the value is a map, adds the item to the menu using the add map function
            is Map<*, *> -> this.addMap(item.value as Map<String, Any>)

        }

        // Adds the menu to the menu bar
        this.menus.add(menu)
    }
}

// Adds the content of the list to the menu
private fun Menu.addList(list: List<Any>, vararg functions: Pair<String, () -> Unit>) {

    // Loops over every item in the list
    for (item in list) {

        // Checks the datatype of the item
        when (item) {

            // If the value is a string, adds an item to the menu
            is String -> this.items.add(MenuItem(item))

            // If the value is a list, adds the item to the menu using the add list function
            is List<*> -> this.addList(item as List<Any>)

            // If the value is a map, adds the item to the menu using the add map function
            is Map<*, *> -> this.addMap(item as Map<String, Any>)
        }
    }
}

// Adds the contents of the map to the menu
fun Menu.addMap(map: Map<String, Any>, vararg functions: Pair<String, () -> Unit>) {

    // Loops over every item in the map
    for (item in map) {
        // Sets the menu to the name of the key
        val menu = Menu(item.key)

        // Checks the datatype of the item
        when (item.value) {

            // If the value is a string, adds an item to the menu
            is String -> menu.items.add(MenuItem(item.value as String))

            // If the value is a list, adds the item to the menu using the add list function
            is List<*> -> menu.addList(item.value as List<Any>)

            // If the value is a map, adds the item to the menu using the add map function
            is Map<*, *> -> menu.addMap(item.value as Map<String, Any>)
        }

        // Adds the created menu to the original menu
        this.items.add(menu)
    }
}