package com.example.shoppinglistassignment2.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistassignment2.MainActivity
import com.example.shoppinglistassignment2.R
import com.example.shoppinglistassignment2.data.ShoppingItem
import kotlinx.android.synthetic.main.edititem_dialog.view.*
import java.lang.RuntimeException

class EditItemDialog : DialogFragment() {

    interface ShoppingItemHandler{
        fun shoppingItemUpdated(shoppingItem: ShoppingItem)
    }

    lateinit var shoppingItemHandler: ShoppingItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ShoppingItemHandler){
            shoppingItemHandler = context
        } else{
            throw RuntimeException(
                "The Activity is not implementing the ShoppingItemHandler intreface"
            )
        }
    }

    lateinit var etItemName: EditText
    lateinit var spinnerCategories: Spinner
    lateinit var etItemDescription: EditText

    lateinit var etEstimatedPrice: EditText
    lateinit var checkBoxAlreadyPurchased: CheckBox

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        //set up categories drop down menu adapter
//        val categoriesAdapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.categories_array, android.R.layout.
//                simple_spinner_item
//        )
//        categoriesAdapter.setDropDownViewResource(
//            android.R.layout.simple_spinner_dropdown_item)
//        spinnerCategories.adapter = categoriesAdapter
//        spinnerCategories.onItemSelectedListener = this

        // Set up dailog
        val dialogBuilder = AlertDialog.Builder(requireContext())


        dialogBuilder.setTitle("Edit Item Dialog")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.edititem_dialog, null

        )

        // set vars that were declared earlier to xml objects
        etItemName = dialogView.etItemName
        spinnerCategories = dialogView.spinnerCategories
        etEstimatedPrice = dialogView.etEstimatedPrice
        etItemDescription = dialogView.etItemDescription
        checkBoxAlreadyPurchased = dialogView.checkBoxAlreadyPurchased

        dialogBuilder.setView(dialogView)
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)) {
            val shoppingItem = arguments.getSerializable(MainActivity.KEY_EDIT) as ShoppingItem
            etItemName.setText(shoppingItem.name)
            spinnerCategories.setSelection(shoppingItem.category)
            etEstimatedPrice.setText(shoppingItem.price.toString())
            etItemDescription.setText(shoppingItem.description)
            checkBoxAlreadyPurchased.isChecked = shoppingItem.status
//            dialogBuilder.setTitle("Edit Item Dialog")


        }



        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->
            shoppingItemHandler.shoppingItemUpdated(
                ShoppingItem(null, etItemName.text.toString(), spinnerCategories.selectedItemPosition,
                    etItemDescription.text.toString(), Integer.parseInt(etEstimatedPrice.text.toString()),
                    checkBoxAlreadyPurchased.isChecked)
            )
        }

        dialogBuilder.setNegativeButton("Cancel") {
                _, _ ->
        }

        return dialogBuilder.create()

    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty() && etEstimatedPrice.text.isNotEmpty()) {
                val arguments = this.arguments
                // IF EDIT MODE
                if (arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)) {
                    handleListItemEdited()
                } else {
                }

                dialog!!.dismiss()
            } else if (etItemName.text.isNotEmpty()){
                etItemName.error = "This field cannot be empty"
            }   else if (etEstimatedPrice.text.isNotEmpty()) {
                etEstimatedPrice.error = "This field cannot be empty"
            }
        }
    }

    private fun handleListItemEdited() {
        val shoppingItemToEdit = arguments?.getSerializable(
            MainActivity.KEY_EDIT
        ) as ShoppingItem
        shoppingItemToEdit.name = etItemName.text.toString()
        shoppingItemToEdit.category = spinnerCategories.selectedItemPosition
        shoppingItemToEdit.price = etEstimatedPrice.text.toString().toInt()
        shoppingItemToEdit.description = etItemDescription.text.toString()
        shoppingItemToEdit.status = checkBoxAlreadyPurchased.isChecked

        shoppingItemHandler.shoppingItemUpdated(shoppingItemToEdit)
    }
}