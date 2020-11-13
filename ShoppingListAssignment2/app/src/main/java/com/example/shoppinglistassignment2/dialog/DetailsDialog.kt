package com.example.shoppinglistassignment2.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistassignment2.MainActivity
import com.example.shoppinglistassignment2.R
import com.example.shoppinglistassignment2.data.ShoppingItem
import kotlinx.android.synthetic.main.details_dialog.view.*

class DetailsDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())


        dialogBuilder.setTitle("Details Item Dialog")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.details_dialog, null

        )

        dialogBuilder.setView(dialogView)
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_DETAILS)) {
            val descriptionText = arguments.getSerializable(MainActivity.KEY_DETAILS) as String
            dialogView.tvDescriptionBox.setText("Item description: " + descriptionText)

        }


        dialogBuilder.setPositiveButton("Back") {
                dialog, which ->
        }

        return dialogBuilder.create()
    }


}