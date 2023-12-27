package com.pose.analysis

import com.pose.analysis.App.Companion.mutedTextColor
import com.pose.analysis.App.Companion.textColor
import com.pose.analysis.MainPane.loadingGif
import com.pose.analysis.MainPane.screenWidth
import com.pose.analysis.VideoPane.mediaPlayer
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Slider
import javafx.scene.control.ToggleButton
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.HBox

internal object SliderPane: HBox() {

    // Defines the height of the slider for calculations
    val sliderHeight = 25.0

    // Sets up the play button and its image
    var playButton = ToggleButton()
    var playImage = ImageView()

    // Sets up the two possible images for the playImage
    var playIcon = Image("play-icon.png")
    var pauseIcon = Image("pause-icon.png")


    val timeSlider = Slider(0.0, 0.0, 0.0)



    init {

        // Moves the play button to the correct location
        playButton.layoutX = -(sliderHeight / 7.5)
        playButton.layoutY = -(sliderHeight / 7.5)

        timeSlider.prefHeight = sliderHeight
        timeSlider.prefWidth = screenWidth * 0.95

        timeSlider.layoutY = (sliderHeight / 2) - (timeSlider.height / 2)
        timeSlider.layoutX = playButton.layoutX + (sliderHeight / 2)

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
            playButton,
            timeSlider
        )

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