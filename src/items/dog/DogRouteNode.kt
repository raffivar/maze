package items

import map.Room

data class DogRouteNode(
    val room: Room,
    var next: DogRouteNode?
)
