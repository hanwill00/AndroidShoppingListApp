package com.example.shoppinglistassignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglistassignment2.adapter.ShoppingListAdapter
import com.example.shoppinglistassignment2.data.ShoppingItem
import kotlinx.android.synthetic.main.activity_createitem.*

class CreateItemActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createitem)

        val categoriesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.categories_array, android.R.layout.
                simple_spinner_item
        )
        categoriesAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = categoriesAdapter
        spinnerCategories.onItemSelectedListener = this

        btnSave.setOnClickListener {
            if(TextUtils.isEmpty(etItemName.getText().toString())){
                Toast.makeText(this, "Please enter an item name!", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(etEstimatedPrice.getText().toString())){
                Toast.makeText(this, "Please enter an estimated price!", Toast.LENGTH_LONG).show()
            }
            else {



                var intentNewItem = Intent()

                intentNewItem.putExtra("ITEM_NAME", etItemName.text.toString())
                intentNewItem.putExtra("ITEM_CATEGORY", spinnerCategories.selectedItemPosition)
                intentNewItem.putExtra("ITEM_DESCRIPTION", etItemDescription.text.toString())

                intentNewItem.putExtra("ITEM_PRICE", Integer.parseInt(etEstimatedPrice.text.toString()))
                intentNewItem.putExtra("ITEM_STATUS", checkBoxAlreadyPurchased.isChecked)


                setResult(2, intentNewItem)
                finish()

            }
        }
    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View?, position: Int, id: Long) {

    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
        super.onBackPressed()
    }
}