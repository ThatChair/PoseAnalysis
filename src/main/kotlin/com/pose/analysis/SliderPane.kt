package com.pose.analysis

import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.App.Companion.textColor
import com.pose.analysis.MainPane.loadingGif
import com.pose.analysis.MainPane.screenWidth
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.ToggleButton
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.StrokeLineCap

object SliderPane: Pane() {

    // Defines the height of the slider for calculations
    val sliderHeight = 25.0

    // Sets up the play button and its image
    var playButton = ToggleButton()
    var playImage = ImageView()

    // Sets up the two possible images for the playImage
    var playIcon = Image("play-icon.png")
    var pauseIcon = Image("pause-icon.png")

    // Sets up the slider point and line
    var sliderPoint = Circle()
    var sliderLine = Line()

    // Sets the property that controls the slider point
    var sliderPointPos: DoubleProperty = SimpleDoubleProperty(120.0)

    init {
        // Gets the slider point to the right position and size
        sliderPoint.centerY = sliderHeight / 2.0
        sliderPoint.centerXProperty().bind(sliderPointPos)
        sliderPoint.radius = sliderHeight / 1.75
        sliderPoint.fill = textColor

        // Sets the start of the slider
        sliderLine.startX = sliderHeight * 2.0

        // Makes sure the slider point is at the beginning of the slider
        sliderPointPos.set(sliderLine.startX)

        // Sets the slider to the right place and size
        sliderLine.startY = sliderHeight / 2.0
        sliderLine.endX = screenWidth - (sliderHeight / 2.0)
        sliderLine.endY = sliderLine.startY
        sliderLine.strokeWidth = sliderHeight / 2.0
        sliderLine.strokeLineCap = StrokeLineCap.ROUND
        sliderLine.stroke = mutedTextColor

        // Moves the play button to the correct location
        playButton.layoutX = -(sliderHeight / 7.5)
        playButton.layoutY = -(sliderHeight / 7.5)

        // Sets the play button graphic
        playButton.graphic = playImage

        // Sets the play button image to change based off of isAnimationPlaying
        playImage.imageProperty().bind(
            Bindings
                .`when`(isAnimationPlaying)
                .then(pauseIcon)
                .otherwise(playIcon)
        )

        // Removes the background of the play button
        playButton.background = Background.EMPTY

        // Changes isAnimationPlaying when the button is pressed
        playButton.setOnAction {
            isAnimationPlaying.set(!isAnimationPlaying.get())
        }

        // Scales the play button image accordingly
        playImage.fitHeight = sliderHeight
        playImage.fitWidth = playImage.fitHeight

        // Adds all children to the slider pane
        SliderPane.children.addAll(
            sliderLine,
            sliderPoint,
            playButton
        )

        // Calls this function, which calculates the dragging of the slider (no duh!)
        calculateSliderDrag(sliderPoint, sliderLine, sliderLine.startX, sliderLine.endX)

        // Makes the slider only visible when the app isn't loading and when the welcome text is gone
        SliderPane.visibleProperty().bind(
            Bindings.
                `when`(
                    isWelcome.not()
                    .and(loadingGif.visibleProperty().not())
                )
                .then(true)
                .otherwise(false)
        )

    }
}