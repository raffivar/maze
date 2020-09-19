package items

import data.SavableItemData

interface SavableItem {
    fun loadItem(itemData: SavableItemData)
}