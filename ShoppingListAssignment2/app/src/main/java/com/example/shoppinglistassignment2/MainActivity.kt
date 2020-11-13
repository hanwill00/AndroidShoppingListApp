package com.example.shoppinglistassignment2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shoppinglistassignment2.adapter.ShoppingListAdapter
import com.example.shoppinglistassignment2.data.AppDatabase
import com.example.shoppinglistassignment2.data.ShoppingItem
import com.example.shoppinglistassignment2.dialog.DetailsDialog
import com.example.shoppinglistassignment2.dialog.EditItemDialog
import com.example.shoppinglistassignment2.touch.ShoppingRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EditItemDialog.ShoppingItemHandler {


    lateinit var shoppingListAdapter: ShoppingListAdapter

    companion object {
        const val KEY_EDIT = "KEY_EDIT"
        const val KEY_DETAILS="KEY_DETAILS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar) // tells activity to use toolbar instead of default actionbar


        initRecyclerView()



    }

    private fun initRecyclerView() {
        Thread {
            var shoppingItemList = AppDatabase.getInstance(this).shoppingItemDao().getAllItems()
            runOnUiThread{
                shoppingListAdapter = ShoppingListAdapter(this, shoppingItemList)
                mainRecyclerView.adapter = shoppingListAdapter

                val touchCallbackList = ShoppingRecyclerTouchCallback(shoppingListAdapter)
                val itemTouchHelper = ItemTouchHelper(touchCallbackList)
                itemTouchHelper.attachToRecyclerView(mainRecyclerView)
            }
        }.start()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //not working? not sure why
//        MaterialTapTargetPrompt.Builder(this)
//            .setTarget(R.id.btnAddItem)
//            .setPrimaryText("Create new shopping item")
//            .setSecondaryText("Click here to create new items")
//            .setIcon(R.drawable.newitem_icon)
//            .show()

        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnAddItem) {
            var intentAddItem = Intent()
            intentAddItem.setClass(this, CreateItemActivity::class.java)
            startActivityForResult(intentAddItem, 1)



        } else if (item.itemId == R.id.btnDeleteAll){
            Thread{
                AppDatabase.getInstance(this).shoppingItemDao().deleteAllItems()

                runOnUiThread{
                    shoppingListAdapter.deleteList()
                }
            }.start()

            Toast.makeText(this, "All of your items were deleted.", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == 2) {
                var itemName = data?.getStringExtra("ITEM_NAME")
                var itemCategory = data?.getIntExtra("ITEM_CATEGORY", 0)

                var itemDescription = data?.getStringExtra("ITEM_DESCRIPTION")

                var itemPrice = data?.getIntExtra("ITEM_PRICE", 0)

                var itemStatus = data?.getBooleanExtra("ITEM_STATUS", false)
                var newShoppingItem = ShoppingItem(
                    null,
                    itemName!!,
                    itemCategory!!,
                    itemDescription!!,
                    itemPrice!!,
                    itemStatus!!
                )

                saveShoppingItem(newShoppingItem)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    var editIndex: Int = -1
    fun showEditShoppingItemDialog(itemToEdit: ShoppingItem, index: Int){
        editIndex = index
        val editItemDialog =
            EditItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager, "EDITDIALOG")

//        EditItemDialog().show(supportFragmentManager, "Dialog")
    }

    var detailsIndex: Int = -1
    fun showDetailsDialog(itemDescription: String, index: Int){
        detailsIndex = index
        val detailsDialog =
            DetailsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_DETAILS, itemDescription)
        detailsDialog.arguments = bundle

        detailsDialog.show(supportFragmentManager, "DETAILSDIALOG")

//        EditItemDialog().show(supportFragmentManager, "Dialog")
    }

    fun saveShoppingItem(shoppingItem: ShoppingItem) {
        Thread{
            shoppingItem.shoppingItemId = AppDatabase.getInstance(this).shoppingItemDao().insertItem(shoppingItem)

            runOnUiThread{
                shoppingListAdapter.addItem(shoppingItem)
            }
        }.start()
    }

    fun query(){
        Thread{
            val shoppingItems = AppDatabase.getInstance(this).shoppingItemDao().getAllItems()

            runOnUiThread {

            }
        }
    }

    override fun shoppingItemUpdated(shoppingItem: ShoppingItem) {
        Thread{
            AppDatabase.getInstance(this).shoppingItemDao().updateItem(shoppingItem)
            runOnUiThread{
                shoppingListAdapter.updateShoppingItem(shoppingItem, editIndex)
            }
        }.start()
    }

}
