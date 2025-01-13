package com.example.project.ui
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.project.data.model.Item
import com.example.project.data.IItemsRepo
import com.example.project.data.impl.ItemsRepo
import kotlinx.coroutines.launch


class MainScreenViewModel: ViewModel() {
    private lateinit var _itemRepo: IItemsRepo
    private lateinit var _items : MutableState<List<Item>>
    private var items = _items

    init{
        viewModelScope.launch {
            _itemRepo = ItemsRepo()
            _items = mutableStateOf(_itemRepo.getItems())
            items = _items
        }
    }

    fun onDelete(item: Item){
        viewModelScope.launch {
            _itemRepo.deleteItem(item)
            _items.value = _itemRepo.getItems()
        }

    }

}