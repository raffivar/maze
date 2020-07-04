import items.Item
import map.Room
import java.util.*

class Player {
    lateinit var currRoom: Room
    val inventory =  TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val inventory = HashMap<String, Item>()

    fun take(item: Item) {
        inventory[item.name] = item
        currRoom.removeItem(item)
    }
}