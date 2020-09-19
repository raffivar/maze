package data

class DogData(name: String,
              val currentNode: DogRouteNodeData,
              val isMoving: Boolean) : SavableItemData(name)