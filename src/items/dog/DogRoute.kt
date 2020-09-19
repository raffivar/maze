package items.dog

import java.util.*

class DogRoute(vararg dogRouteNodes: DogRouteNode): TreeMap<String, DogRouteNode>(String.CASE_INSENSITIVE_ORDER) {
    init {
        for (dogRouteNode in dogRouteNodes) {
            add(dogRouteNode)
        }
    }

    fun add(node: DogRouteNode) {
        this[node.nodeId] = node
    }
}
