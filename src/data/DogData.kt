package data

class DogData(
    name: String,
    description: String,
    private val currentNode: DogRouteNodeData,
    private val isMoving: Boolean) : ItemData(name, description)