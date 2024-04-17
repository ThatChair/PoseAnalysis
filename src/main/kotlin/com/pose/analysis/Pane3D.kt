package com.pose.analysis

import com.pose.analysis.MainPane.middlePane
import com.pose.analysis.resources.classes.Connections
import com.pose.analysis.resources.extensions.round
import com.pose.analysis.resources.functions.render
import javafx.beans.binding.Bindings
import javafx.geometry.Point3D
import javafx.scene.layout.Pane


object Pane3D : Pane() {


    // Standard mediapipe connections without the head...
    // This sets up the lines connecting the dots of the 3D render.
    // Each array is the connections for the dot at that index (i.e. index on connects to index 12, 13, & 23)
    // The first couple are commented out because the head is not rendered
    private val personConnections = Connections(
//        mutableListOf(2, 5),
//        mutableListOf(),
//        mutableListOf(7),
//        mutableListOf(),
//        mutableListOf(),
//        mutableListOf(8),
//        mutableListOf(),
//        mutableListOf(),
//        mutableListOf(),
//        mutableListOf(10),
//        mutableListOf(),
        mutableListOf(12, 13, 23),
        mutableListOf(14, 24),
        mutableListOf(15),
        mutableListOf(16),
        mutableListOf(17, 19, 21),
        mutableListOf(18, 20, 22),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(24, 25),
        mutableListOf(26),
        mutableListOf(27),
        mutableListOf(28),
        mutableListOf(29, 31),
        mutableListOf(30, 32),
        mutableListOf(31),
        mutableListOf(32),
        mutableListOf(),
        mutableListOf()
    )

    // Specifies the camera position and the screen position in 3d space
    val cameraPos = Point3D(0.0, 0.0, 5.0)
    private const val FOCAL_LENGTH = 3.0

    // Stores the x and y angle the person is rotated at
    private var yAngle = 0.0
    private var xAngle = 0.0

    // Stores the x and y angle offsets so the person is oriented correctly
    private var yAngleOffset = Math.PI
    private var xAngleOffset = 0.0

    // Stores the zoom level
    private var zoom = 200.0
        set(value) {
            field = if (value > 0) value else 0.0
        }


    // Stores the previous mouse x and y coordinates so the x and y angles are incremented
    private var lastX = 0.0
    private var lastY = 0.0

    init {

        // Specifies the position of the Pane3D
        Pane3D.layoutX = middlePane.prefWidth / 2.0
        Pane3D.layoutY = middlePane.prefHeight / 2.0


        // Makes sure the Pane3D is only visible when the welcome message is gone and the app isn't loading
        Pane3D.visibleProperty().bind(
            Bindings.`when`(
                isWelcome.not()
                    .and(MainPane.loadingGif.visibleProperty().not())
            )
                .then(true)
                .otherwise(false)
        )

        // Set up the mouse drag event handler
        Pane3D.setOnMousePressed { event ->
            lastX = event.sceneX
            lastY = event.sceneY
        }

        // Calculates how much to change the angleX and angleY when the mouse is dragged
        Pane3D.setOnMouseDragged { event ->

            // Gets the change in mouse position
            val deltaX = event.sceneX - lastX
            val deltaY = event.sceneY - lastY

            // Uses the change in mouse position to change the angleX and angleY
            yAngle += deltaY * Math.PI / 180
            xAngle += deltaX * Math.PI / 180

            // Re-renders the person
            renderPerson()
            // Update lastX and lastY for the next frame
            lastX = event.sceneX
            lastY = event.sceneY
        }

        // Handles touchscreen zooming
        Pane3D.setOnZoom { event ->
            zoom *= event.zoomFactor
            renderPerson()
        }

        // Handles trackpad/scroll zooming
        Pane3D.setOnScroll { event ->
            zoom += event.deltaX + event.deltaY
            renderPerson()
        }
    }

    fun renderPerson() {
        this.render(
            animation,
            personConnections,
            xAngle + xAngleOffset,
            yAngle + yAngleOffset,
            currentFrame,
            cameraPos,
            FOCAL_LENGTH,
            zoom
        )

        if (MainMenuBar.viewingAngularVelocity.get()) {
            MainPane.angularVelocityText.text = "Arm Angular Velocities (Radians / Second):\n" +
                    "Left: ${currentLeftAngularVelocityRadiansPerSecond.round(3)}\n" +
                    "Right: ${currentRightAngularVelocityRadiansPerSecond.round(3)}"
        }
    }
}