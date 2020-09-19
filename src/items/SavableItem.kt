package items

import data.ItemData

interface SavableItem {
    fun loadItem(itemData: ItemData)
}