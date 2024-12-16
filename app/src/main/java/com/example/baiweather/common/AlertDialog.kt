package com.example.baiweather.common

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialog(private val title:String, private val description: String, private val positiveText:String, private val neutralText:String) : DialogFragment() {

    lateinit var positiveClick: () -> Unit
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage(description)
                .setTitle(title)
                .setPositiveButton(positiveText) { _, _ ->
                    positiveClick.invoke()
                }
                .setNeutralButton(neutralText) { _, _ ->
                    dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Unexpected exception")
    }
}