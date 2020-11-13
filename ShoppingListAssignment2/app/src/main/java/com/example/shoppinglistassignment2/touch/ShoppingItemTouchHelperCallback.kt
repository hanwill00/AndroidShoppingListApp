package com.example.shoppinglistassignment2.touch

interface ShoppingItemTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}