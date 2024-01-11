package com.pose.analysis

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.pose.analysis.ErrorPane.showError
import com.pose.analysis.Pane3D.render
import com.pose.analysis.Pane3D.zoom
import javafx.animation.AnimationTimer
import javafx.beans.property.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import java.io.File
import kotlin.properties.Delegates

// BooleanProperty to keep track when WelcomePane should be displayed
var isWelcome: BooleanProperty = SimpleBooleanProperty(true)

// BooleanProperty to keep track when an animation is playing
var isAnimationPlaying: BooleanProperty = SimpleBooleanProperty(false)

// Saves the loaded animation, an array of frames which are arrays of points which are arrays of coordinates.
// Lots of arrays lol
lateinit var animation: Array<Array<D1Array<Double>>>

// I do not remember what this does :(
var fps by Delegates.notNull<Double>()

// From 0-1
// Specifies the percentage of the way through the animation you are
var animPercent: DoubleProperty = SimpleDoubleProperty(0.0)

// Specifies the frame number you are currently on.
var frameNumber: IntegerProperty = SimpleIntegerProperty(1)

// Handles the animation
val animationTimer = object : AnimationTimer() {

    // Variable to store the last update time
    private var lastUpdateTime: Long = 0

    // Property to get the frame delay in milliseconds based on the desired frames per second (fps)
    private val frameDelayMillis: Long
        get() = (1 / fps * 1000).toLong()

    // Override the handle method to define the behavior of the animation timer
    override fun handle(now: Long) {

        // Check if the animation is currently playing
        if (isAnimationPlaying.get()) {

            // Check if enough time has passed since the last update
            if (now - lastUpdateTime >= frameDelayMillis * 1_000_000) {

                // Calculate the animation percentage based on the current frame and total frames
                animPercent.set(((currentFrame + 2.0) / frameNumber.get()))

                // Check if the animation has reached or exceeded 100%
                if (animPercent.get() >= 1.0) {
                    // Stop the animation and set the percentage to 100%
                    isAnimationPlaying.set(false)
                    animPercent.set(1.0)
                }

                // Perform rendering with the current frame and zoom level
                render(currentFrame, zoom)

                // Update the last update time
                lastUpdateTime = now
            }
        }
    }
}

// Just gets the current frame. What else would it do?
val currentFrame: Int
    get() = (animPercent.get() * (frameNumber.get())).toInt()


// Loads an animation from the json file at the given path and updates the animation variable accordingly
fun loadAnimation(path: String) {
    val jsonString = File(path).readText()

    // Create a Gson instance
    val gson = Gson()

    // Parse the JSON string into a JsonElement
    val jsonElement: JsonElement = gson.fromJson(jsonString, JsonElement::class.java)

    // Ensure that the top-level structure is a JsonObject
    if (jsonElement.isJsonArray) {
        val jsonArray = jsonElement.asJsonArray

        // Extract the first element (double) from the initial array
        if (jsonArray.size() > 0 && jsonArray[0].isJsonPrimitive && jsonArray[0].asJsonPrimitive.isNumber) {
            // Gets the fps from the file
            fps = jsonArray[0].asJsonPrimitive.asDouble

            // Initializes the result array
            val result = mutableListOf<Array<DoubleArray>>()

            // Iterate over the entries in the JsonObject, skipping the first element
            for (i in 1 until jsonArray.size()) {
                // Checks to make sure it is the right type
                if (jsonArray[i].isJsonArray) {
                    // Gets the inner array, which contains points
                    val innerArray = jsonArray[i].asJsonArray

                    // Convert the JsonArray to a DoubleArray
                    val doubleArray = Array(innerArray.size()) { j ->
                        val subArray = innerArray[j].asJsonArray
                        DoubleArray(subArray.size()) { k -> subArray[k].asDouble }
                    }

                    // Adds the double array to the result
                    result.add(doubleArray)
                }
            }

            // Maps animation to the result
            animation = result.map { frame ->
                frame.map {
                    mk.ndarray(mk[it[0], it[1], it[2]])
                }.toTypedArray()
            }.toTypedArray()

            // Sets the total number of frames
            frameNumber = SimpleIntegerProperty(animation.size)

            // Renders the new animation
            render(currentFrame, zoom)

        } else {
            // Shows an error if there is no fps
            showError(
                "First element is not a double in the initial array.",
                "Make sure your JSON file is formatted correctly (ie. generated by this app)"
            )
        }
    } else {
        // Shows error if it isn't a json array
        showError("Not a JSON array", "Make sure your JSON file is formatted correctly (ie. generated by this app)")
    }

}

