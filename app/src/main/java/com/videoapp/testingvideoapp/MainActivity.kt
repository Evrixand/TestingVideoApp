package com.videoapp.testingvideoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.videoapp.testingvideoapp.data.repository.user.UserRepository
import com.videoapp.testingvideoapp.databinding.ActivityMainBinding
import com.videoapp.testingvideoapp.infrasctructure.DialogSupport.showLoginDialog
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val repository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserInSystem()
    }

    private fun checkUserInSystem() {

        if (repository.isUserInSystem()) {
            Toast.makeText(this, getString(R.string.logged_in_lable), Toast.LENGTH_LONG).show()
        }
        else {
            showLoginDialog(this)
        }
    }
}