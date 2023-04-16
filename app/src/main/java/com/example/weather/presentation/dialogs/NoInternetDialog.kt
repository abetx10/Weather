package com.example.weather.presentation.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.weather.R

object NoInternetDialog {
    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.no_internet_connection_title))
        builder.setMessage(context.getString(R.string.no_internet_connection_message))
        builder.setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}