package com.videoapp.testingvideoapp.infrasctructure

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.databinding.LoginDialogBinding

object DialogSupport {

    fun showLoginDialog(context: AppCompatActivity) {

        val view = LoginDialogBinding.inflate(context.layoutInflater).root

        val loginBtn = view.findViewById<MaterialButton>(R.id.btn_login)
        val guestBtn = view.findViewById<MaterialButton>(R.id.btn_guest)

        val builder = AlertDialog.Builder(context)
            .setView(view)
        val alertDialog = builder.create()

        loginBtn.setOnClickListener {

            context.findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_login)
            alertDialog.dismiss()
        }
        guestBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}