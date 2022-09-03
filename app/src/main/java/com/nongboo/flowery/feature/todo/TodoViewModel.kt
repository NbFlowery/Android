package com.nongboo.flowery.feature.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nongboo.flowery.model.TodoData


enum class ItemNotify {
    ADD, UPDATE, DELETE
}

class TodoViewModel : ViewModel() {
    val itemsData = MutableLiveData<ArrayList<TodoData>>() //액티비티와 프래그먼트가 공유할 값
    private val items = ArrayList<TodoData>()
    var longClickItem: Int = -1

    var itemNotified: Int = -1
    var itemNotifiedType: ItemNotify = ItemNotify.ADD

    //초기화 처음
    init {
        addItem(TodoData("더블탭하면 완료한 일이 됩니다."))
        addItem(TodoData("50% 완료한 일은 반만 채워집니다."))
        addItem(TodoData("할 일을 적는 칸입니다."))
    }

    fun getItem(pos: Int) = items[pos]

    fun addItem(item: TodoData) {
        itemNotifiedType = ItemNotify.ADD
        //itemNotified = items.size
        items.add(item)
        itemsData.value = items
    }

    fun updateItem(item: TodoData, pos: Int) {
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
        return if (items.size == 0) 0 else finishCnt * 100 / items.size
    }
}