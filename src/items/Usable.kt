package items

interface Usable {
    val itemToUseOn: HashSet<Item>
        get() = HashSet()

    fun useOn(item: Item)
    fun addItemToUseOn(itemItem: Item)
}