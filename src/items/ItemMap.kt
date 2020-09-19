package items

import java.util.*

class ItemMap : TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER) {
    fun add(item: Item) {
        this[item.name] = item
    }

    fun removeItem(item: Item) {
        remove(item.name)
    }
}