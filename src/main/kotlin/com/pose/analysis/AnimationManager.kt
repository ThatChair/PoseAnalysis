package com.pose.analysis

import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.io.File

// BooleanProperty to keep track when WelcomePane should be displayed
var isWelcome: BooleanProperty = SimpleBooleanProperty(true)

// BooleanProperty to keep track when an animation is playing
var isAnimationPlaying: BooleanProperty = SimpleBooleanProperty(false)

var fileLoaded: BooleanProperty = SimpleBooleanProperty(false)

var fileDisplayed: BooleanProperty = SimpleBooleanProperty(false)

// From 0-1
var videoPercent: DoubleProperty = SimpleDoubleProperty(0.0)

var videoLength: Duration = Duration(0.0)

var selectedFile: File? = null

fun MediaPlayer.jumpToPercentage(percentage: Double) {
    val totalDuration = this.totalDuration
    val targetPosition = Duration(totalDuration.toMillis() * percentage)
    this.seek(targetPosition)
    this.currentTimeProperty()
}
