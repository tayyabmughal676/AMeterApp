package com.example.ameterapp

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*

class ExerciseActivity : AppCompatActivity(), SensorEventListener {
    //  Declared Buttons
    private lateinit var mStartExerciseBtn: Button
    private lateinit var mSubmitExerciseBtn: Button

    // Declared Name, Timer
    private lateinit var mSetExerciseName: TextView
    private lateinit var mExerciseTimer: TextView

    //  Declared X, Y, Z
    private lateinit var mValueNumbOfX: TextView
    private lateinit var mValueNumbOfY: TextView
    private lateinit var mValueNumbOfZ: TextView

    //     Declared Sensor
    private lateinit var sensorManager: SensorManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
//        Sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//      Default Sensor
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
//        Initialized View
        initView()
        isExternalStorageReadAble()
        isExternalStorageWriteAble()
        writeFile()
    }

    private fun writeFile() {
        if(isExternalStorageWriteAble()){
            val file : File = File(Environment.getExternalStorageDirectory(),"HELLO")
        }
    }

    private fun isExternalStorageWriteAble(): Boolean {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Toast.makeText(applicationContext, "Writeable", Toast.LENGTH_SHORT).show()
            true
        } else {
            Toast.makeText(applicationContext, "Not Able to write", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun isExternalStorageReadAble(): Boolean {
        return if (Environment.MEDIA_MOUNTED ==
            Environment.getExternalStorageState() ||
            Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()
        ) {
            Toast.makeText(applicationContext, "Readable", Toast.LENGTH_SHORT).show()
            true
        } else {
            Toast.makeText(applicationContext, "Not Able To Read", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun initView() {
        //      Initialized Get Intent Data
        val userExerciseName = intent!!.getStringExtra("UserMessage")
        Toast.makeText(applicationContext, "$userExerciseName", Toast.LENGTH_SHORT).show()

        //        Initialized X, Y, Z
        mValueNumbOfX = findViewById(R.id.valueNumOfX)
        mValueNumbOfY = findViewById(R.id.valueNumOfY)
        mValueNumbOfZ = findViewById(R.id.valueNumOfZ)
//        Initialized Name, Timer
        mSetExerciseName = findViewById(R.id.ExerciseNameText)
        mSetExerciseName.text = userExerciseName

        mExerciseTimer = findViewById(R.id.ExerciseTimer)
//        Initialized Buttons
        mSubmitExerciseBtn = findViewById(R.id.submitExerciseDataBtn)
        mStartExerciseBtn = findViewById(R.id.startBtn)
//        Set Clicks
        mStartExerciseBtn.setOnClickListener {
            startExerciseNow()
        }
        mSubmitExerciseBtn.setOnClickListener {
            submitExerciseData()
        }
    }

    //    Start Exercise Now Function
    private fun startExerciseNow() {

    }

    //    Submit Exercise Data to CSV File
    private fun submitExerciseData() {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("SENSORS", "onSensorChanged: The values are ${Arrays.toString(event?.values)}")

        val mX = event?.values?.get(0)
        val mY = event?.values?.get(1)
        val mZ = event?.values?.get(2)

        mValueNumbOfX.text = mX.toString()
        mValueNumbOfY.text = mY.toString()
        mValueNumbOfZ.text = mZ.toString()

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }
}