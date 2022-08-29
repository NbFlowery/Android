package com.nongboo.flowery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Item( val name: String, var state: Boolean = false)
enum class ItemNotify {
    ADD, UPDATE, DELETE
}

class MyViewModel : ViewModel() {
    val itemsData = MutableLiveData<ArrayList<Item>>() //액티비티와 프래그먼트가 공유할 값
    private val items = ArrayList<Item>()
    var longClickItem: Int = -1

    var itemNotified: Int = -1
    var itemNotifiedType: ItemNotify = ItemNotify.ADD

    //초기화 처음
    init {
        addItem(Item("더블탭하면 완료한 일이 됩니다."))
        addItem(Item("50% 완료한 일은 반만 채워집니다."))
        addItem(Item("할 일을 적는 칸입니다."))
    }

    fun getItem(pos: Int) = items[pos]

    fun addItem(item: Item) {
        itemNotifiedType = ItemNotify.ADD
        //itemNotified = items.size
        items.add(item)
        itemsData.value = items
    }

    fun updateItem(item: Item, pos: Int) {
        itemNotifiedType = ItemNotify.UPDATE
        //itemNotified = pos
        items[pos] = item
        itemsData.value = items

    }

    fun deleteItem(pos: Int) {
        itemNotifiedType = ItemNotify.DELETE
        itemNotified = pos
        items.removeAt(pos)
        itemsData.value = items
    }

    fun changeItemState(pos: Int) {
        items[pos].state = !items[pos].state
        itemsData.value = items
    }

    fun getItemsPercent(): Int {
        var finishCnt = 0
        for (item in items) {
            if (item.state) finishCnt += 1
        }
        return if (items.size == 0) 0 else finishCnt*100/items.size
    }
}