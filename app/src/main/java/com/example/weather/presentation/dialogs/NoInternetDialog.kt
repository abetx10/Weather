package com.example.weather.presentation.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog

object NoInternetDialog {
    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please check your internet connection to update the data.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}