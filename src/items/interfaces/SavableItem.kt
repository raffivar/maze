package items.interfaces

import data.items.ItemData

interface SavableItem {
    fun loadItem(itemData: ItemData)
}