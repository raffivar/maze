package items.dog

import map.Room

data class DogRouteNode(
    val nodeId: String,
    val room: Room,
    var next: DogRouteNode?
)
