package com.rahul.assignment

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var imageView: ImageView
    private lateinit var editText: EditText
    private val viewModel: DogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //views
        imageView = findViewById(R.id.imageView)
        editText = findViewById(R.id.editText)
        findViewById<Button>(R.id.btnPrevious).setOnClickListener(this)
        findViewById<Button>(R.id.btnNext).setOnClickListener(this)
        findViewById<Button>(R.id.btnSubmit).setOnClickListener(this)

        observeData()

    }

    private fun observeData() {
        viewModel.getNextImage().observe(this) {
            Glide.with(this).load(it).placeholder(android.R.drawable.picture_frame).into(imageView)
        }

        viewModel.errorLive.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPrevious -> {

            }

            R.id.btnNext -> {
                viewModel.doAction()
            }

            R.id.btnSubmit -> {
                val data =  Integer.parseInt(editText.text.toString())
                if (data in 1..10) {
                    viewModel.getImages(data)
                } else {
                    Toast.makeText(this, "Incorrect input", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}