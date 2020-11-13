package com.example.shoppinglistassignment2.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistassignment2.MainActivity
import com.example.shoppinglistassignment2.R
import com.example.shoppinglistassignment2.data.AppDatabase
import com.example.shoppinglistassignment2.data.ShoppingItem
import com.example.shoppinglistassignment2.touch.ShoppingItemTouchHelperCallback
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*


class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>, ShoppingItemTouchHelperCallback {
    private var bookIcon: Bitmap
    private var foodIcon: Bitmap
    private var electronicIcon: Bitmap


    private val context: Context
    private var shoppingItems: MutableList<ShoppingItem> = mutableListOf<ShoppingItem>()

    constructor(context: Context, listShoppingItems: List<ShoppingItem>){
        this.context = context

        shoppingItems.addAll(listShoppingItems)

        bookIcon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.book_icon)

        foodIcon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.food_icon)

        electronicIcon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.electronic_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_row, parent, false
        )
        return ViewHolder(view)


    }
    override fun onBindViewHolder(holder: ShoppingListAdapter.ViewHolder, position: Int) {
        val currentShoppingItem = shoppingItems[position]
        holder.tvItemName.text = currentShoppingItem.name
        holder.tvItemPrice.text = currentShoppingItem.price.toString()
        holder.chkBoxStatus.isChecked = currentShoppingItem.status

        holder.btnDeleteItem.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as MainActivity).showEditShoppingItemDialog(
                shoppingItems[holder.adapterPosition], holder.adapterPosition
            )
        }

        holder.chkBoxStatus.setOnClickListener{
            shoppingItems[holder.adapterPosition].status = holder.chkBoxStatus.isChecked
            Thread{
                AppDatabase.getInstance(context).shoppingItemDao().updateItem(shoppingItems[holder.adapterPosition])
            }.start()
        }

        holder.btnDetails.setOnClickListener {
            (context as MainActivity).showDetailsDialog(
                shoppingItems[holder.adapterPosition].description, holder.adapterPosition

            )
        }

        if(currentShoppingItem.category == 0){
            holder.ivItemIcon.setImageBitmap(foodIcon)
        } else if (currentShoppingItem.category == 1){
            holder.ivItemIcon.setImageBitmap(bookIcon)
        } else if(currentShoppingItem.category == 2){
            holder.ivItemIcon.setImageBitmap(electronicIcon)

        }

    }


    public fun addItem(shoppingItem: ShoppingItem){
        shoppingItems.add(shoppingItem)
        notifyItemInserted(shoppingItems.lastIndex)

    }

    public fun deleteList() {
        shoppingItems.clear()
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int){
        Thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteItem(
                shoppingItems.get(position)
            )
            (context as MainActivity).runOnUiThread{
                shoppingItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    public fun updateShoppingItem(shoppingItem: ShoppingItem, editIndex: Int) {
        shoppingItems.set(editIndex, shoppingItem)
        notifyItemChanged(editIndex)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvItemName = itemView.tvItemName
        val tvItemPrice = itemView.tvItemPrice
        val ivItemIcon = itemView.ivItemIcon
        val chkBoxStatus = itemView.chkboxStatus
        val btnDeleteItem = itemView.btnDeleteItem
        val btnEdit = itemView.btnEdit
        val btnDetails = itemView.btnDetails
    }

}