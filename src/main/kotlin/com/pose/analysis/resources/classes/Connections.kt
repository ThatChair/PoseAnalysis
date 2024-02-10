package com.pose.analysis.resources.classes


class Connections(private vararg var connections: MutableList<Int>) {
    fun addConnection(pointID: Int, connectionID: Int) {
        connections[pointID].add(connectionID)
    }

    fun getConnections(pointID: Int): List<Int> {
        return connections[pointID]
    }
}