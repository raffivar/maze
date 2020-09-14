package data

class DoorData(
    name: String,
    description: String,
    private val isClosed: Boolean) : ItemData(name, description)