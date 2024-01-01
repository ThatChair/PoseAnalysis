package com.pose.analysis

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.pose.analysis.`3DPane`.render
import com.pose.analysis.`3DPane`.zoom
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

lateinit var animation: Array<Array<D1Array<Double>>>

var fps by Delegates.notNull<Double>()

// From 0-1
var animPercent: DoubleProperty = SimpleDoubleProperty(0.0)

var frameNumber: IntegerProperty = SimpleIntegerProperty(1)


val animationTimer = object : AnimationTimer() {
    private var lastUpdateTime: Long = 0
    private val frameDelayMillis: Long
        get() = (1 / fps * 1000).toLong()

    override fun handle(now: Long) {
        if (isAnimationPlaying.get()) {
            if (now - lastUpdateTime >= frameDelayMillis * 1_000_000) {
                animPercent.set(((currentFrame + 2.0) / frameNumber.get()))

                if (animPercent.get() >= 1.0) {
                    isAnimationPlaying.set(false)
                    animPercent.set(1.0)
                }
                render(currentFrame, zoom)

                lastUpdateTime = now
            }
        }
    }
}
val currentFrame: Int
    get() = (animPercent.get() * (frameNumber.get())).toInt()


fun loadAnimation() {
    val jsonString = File("./res/temp/data.json").readText()

    // Create a Gson instance
    val gson = Gson()

    // Parse the JSON string into a JsonElement
    val jsonElement: JsonElement = gson.fromJson(jsonString, JsonElement::class.java)

    // Ensure that the top-level structure is a JsonObject
    if (jsonElement.isJsonArray) {
        val jsonArray = jsonElement.asJsonArray

        // Extract the first element (double) from the initial array
        if (jsonArray.size() > 0 && jsonArray[0].isJsonPrimitive && jsonArray[0].asJsonPrimitive.isNumber) {
            fps = jsonArray[0].asJsonPrimitive.asDouble
            println("First double: $fps")

            val result = mutableListOf<Array<DoubleArray>>()

            // Iterate over the entries in the JsonObject, skipping the first element
            for (i in 1 until jsonArray.size()) {
                if (jsonArray[i].isJsonArray) {
                    val innerArray = jsonArray[i].asJsonArray

                    // Convert the JsonArray to a DoubleArray
                    val doubleArray = Array(innerArray.size()) { j ->
                        val subArray = innerArray[j].asJsonArray
                        DoubleArray(subArray.size()) { k -> subArray[k].asDouble }
                    }

                    result.add(doubleArray)
                }
            }

            animation = result.map { frame ->
                frame.map {
                    mk.ndarray(mk[it[0], it[1], it[2]])
                }.toTypedArray()
            }.toTypedArray()
            frameNumber = SimpleIntegerProperty(animation.size)
            render(currentFrame, zoom)

        } else {
            println("First element is not a double in the initial array.")
        }
    } else {
        println("Not a JSON array")
    }

}

