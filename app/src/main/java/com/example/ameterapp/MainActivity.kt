package com.example.ameterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //    Initialized
    private lateinit var mSubmitBtn: Button
    private lateinit var mGetExerciseName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Gets IDs
        mSubmitBtn = findViewById(R.id.submitBtn)
        mGetExerciseName = findViewById(R.id.exerciseName)

//        Set Click
        mSubmitBtn.setOnClickListener {
//            Get Name
            getExerciseName()
        }
    }

    //    Getting Exercise Name from Edit Field.
    private fun getExerciseName() {

        val name = mGetExerciseName.text.toString().trim()
//        Checking String is empty
        if (name.isEmpty()) {
            mGetExerciseName.error = "Enter Exercise Name"
            return
        }

        Toast.makeText(applicationContext, "Name is: $name", Toast.LENGTH_SHORT).show()
//     Sending Data to Exercise Activity
//        val intent = Intent(applicationContext, ExerciseActivity::class.java)
//        intent.putExtra("UserMessage", name)
//        startActivity(intent)

        val intent = Intent().apply {
            setClass(applicationContext, ExerciseActivity::class.java)
            putExtra("UserMessage", name)
        }
        startActivity(intent)
    }
}