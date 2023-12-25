package com.pose.analysis

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

// BooleanProperty to keep track when WelcomePane should be displayed
var isWelcome: BooleanProperty = SimpleBooleanProperty(true)

// BooleanProperty to keep track when an animation is playing
var isAnimationPlaying: BooleanProperty = SimpleBooleanProperty(false)

