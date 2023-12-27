package com.pose.analysis

import com.pose.analysis.SliderPane.timeSlider
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.util.Duration
import java.io.File

object VideoPane: StackPane() {

    private var video: Media? = null
    var mediaPlayer: MediaPlayer? = null
    private var mediaView: MediaView? = null

    init {
        VideoPane.alignment = Pos.CENTER

        VideoPane.minWidth = MainPane.screenWidth / 4
        VideoPane.prefWidth = MainPane.screenWidth / 2.0
        VideoPane.maxWidth = MainPane.screenWidth * 0.75

        VideoPane.prefHeight = AnimationPane.height
        VideoPane.maxHeight = AnimationPane.height
    }

    fun renderVideo(file: File) {
        if (!fileDisplayed.get()) {
            try {
                if (VideoPane.children.isNotEmpty()) {
                    VideoPane.children.clear()

                    mediaPlayer?.stop()
                    mediaPlayer?.dispose()
                    mediaPlayer = null

                    mediaView = null
                }
                println("Loading ${file.toURI()}")
                video = Media(file.toURI().toString())
                mediaPlayer = MediaPlayer(video)

                mediaPlayer!!.setOnReady {

                    mediaView = MediaView(mediaPlayer)


                    videoLength = mediaPlayer!!.totalDuration

                    mediaView!!.fitWidth = VideoPane.width

                    //VideoPane.maxHeight = video!!.height * (video!!.width / mediaView!!.fitWidth)

                    println(mediaView)
                    VideoPane.children.addAll(mediaView)

                    isAnimationPlaying.addListener {_, _, newPlay ->
                        if (mediaPlayer != null) {
                            if (newPlay) {
                                mediaPlayer!!.play()
                            } else {
                                mediaPlayer!!.pause()
                            }
                        }
                    }

                    timeSlider.max = mediaPlayer!!.totalDuration.toMillis()

                    timeSlider.valueProperty().addListener { _, _, newValue ->
                        Platform.runLater {
                            if (mediaPlayer!!.status != MediaPlayer.Status.PLAYING) {
                                mediaPlayer!!.seek(Duration.millis(newValue.toDouble()))
                            }
                        }
                    }

                    mediaPlayer!!.currentTimeProperty().addListener { _, _, newValue ->
                        Platform.runLater {
                            if (mediaPlayer!!.status == MediaPlayer.Status.PLAYING) {
                                timeSlider.value = newValue.toMillis()
                            }
                        }
                    }

//                    mediaPlayer!!.currentTimeProperty().addListener { _, _, newValue ->
//                        if (mediaPlayer != null) {
//                            if (isAnimationPlaying.get()) {
//                                videoPercent.set(newValue.toMillis() / videoLength.toMillis())
//                            }
//                        }
//                    }

                }

//                videoPercent.addListener { _, _, newValue ->
//                    if (mediaPlayer != null) {
//                        mediaPlayer!!.jumpToPercentage(newValue.toDouble())
//                    }
//                }


            } catch (e: Exception) {
                println("Error while rendering video. Error:")
                e.printStackTrace()
            }
        }
    }
}