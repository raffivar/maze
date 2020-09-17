package data

class DogData(
    name: String,
    private val currentNode: DogRouteNodeData,
    private val isMoving: Boolean) : ItemData(name)