package com.example.revision.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.revision.R

class LoadingUtils(val activity: Activity) {
    lateinit var alertDialog: AlertDialog

    fun showDialog(){
        var dialogView = activity.layoutInflater.inflate(R.layout.loading_dialog,null)
        var builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss(){
        alertDialog.dismiss()
    }
}