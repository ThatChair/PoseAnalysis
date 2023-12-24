package com.pose.analysis

import javafx.scene.control.Label
import javafx.scene.paint.Color

// Sets the color of a label
fun Label.setColor(color: Color): Label {

    // Changes the text fill to the provided color
    this.textFill = color

    // Returns the modified label
    return this
}

// Determines if the app is running from intellij or a JAR
fun isRunningFromJar(): Boolean {
    // Gets the class name
    val className = App::class.java.name.replace(".", "/") + ".class"

    // Gets the classpath
    val classPath = App::class.java.classLoader.getResource(className)?.toString() ?: return false

    // Returns whether the app is running from intellij or a JAR
    return classPath.startsWith("jar:")
}
