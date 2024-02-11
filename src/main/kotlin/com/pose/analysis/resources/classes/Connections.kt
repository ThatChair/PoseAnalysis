package com.pose.analysis.resources.classes


// A simple connections class. It stores lists of connections.
// Each connection consists of 2 parts. The primary dot, and the connections.
// The primary dot is simply the index of the connection
// The connections are the array at the index of the primary dot and they specify the ID of all dots tha connect to the primary dot
class Connections(private vararg var connections: MutableList<Int>) {

    // Appends a new connection / primary dot
    fun addConnection(pointID: Int, connectionID: Int) {
        connections[pointID].add(connectionID)
    }

    // Gets the connections of a specific primary point (by ID)
    fun getConnections(pointID: Int): List<Int> {
        return connections[pointID]
    }
}