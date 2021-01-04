package com.example.ameterapp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ameterapp.Data.ExcelModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ExerciseActivity : AppCompatActivity(), SensorEventListener {
    //  Declared Buttons
    private lateinit var mStartExerciseBtn: Button
    private lateinit var mSubmitExerciseBtn: Button
    private lateinit var mStopBtn: Button

    // Declared Name, Timer
    private lateinit var mSetExerciseName: TextView
    private lateinit var mExerciseTimer: TextView

    //  Declared X, Y, Z
    private var mValueNumbOfX: TextView? = null
    private var mValueNumbOfY: TextView? = null
    private var mValueNumbOfZ: TextView? = null

    //     Declared Sensor
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private lateinit var countDownTimer: CountDownTimer

    private val CSV_HEADER = "ValueOfX,ValueOfY,ValueOfZ"
//    val TAG: String = "ExecriseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

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
        mStopBtn = findViewById(R.id.stopBtn)

        mSubmitExerciseBtn.setOnClickListener {
            submitExerciseData()
        }
//        Sensor
        //        Set Clicks
        mStartExerciseBtn.setOnClickListener {
            startExerciseNow()

        }
        mStopBtn.setOnClickListener {
            stopAction()
        }

//      Initialized View
        initSensor()
    }

    private fun initSensor() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (mAccelerometer == null) {
            Toast.makeText(this, "Accelerometer sensor not available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //    Start Exercise Now Function
    private fun startExerciseNow() {
        //      Default Sensor
        mSensorManager?.registerListener(
            this,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        startMyCounter()
        countDownTimer.start()
    }

    private fun startMyCounter() {
        countDownTimer = object : CountDownTimer(30000 + 100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val counter = millisUntilFinished / 1000;
                mExerciseTimer.text = counter.toString()
            }

            override fun onFinish() {
                callOnFinish()
            }
        }
    }

    private fun callOnFinish() {
        mSensorManager?.unregisterListener(this)
    }

    private fun stopAction() {
        countDownTimer.cancel()
        mSensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
//        Log.d("SENSORS", "onSensorChanged: The values are ${Arrays.toString(event?.values)}")
        Log.d("SENSORS", "onSensorChanged: The values are ${Arrays.toString(event?.values)}")

        val mX = event?.values?.get(0).toString()
        val mY = event?.values?.get(1).toString()
        val mZ = event?.values?.get(2).toString()

        mValueNumbOfX?.text = mX
        mValueNumbOfY?.text = mY
        mValueNumbOfZ?.text = mZ

//        val myValues = listOf(
//                ExcelModel(0, mX, mY, mZ)
//        )

        val entry: String = "$mX , $mY , $mZ"

//        saveAndDisplayValues(mX, mY, mZ)
    }

    //    Submit Exercise Data to CSV File
    private fun submitExerciseData() {
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
    }

//    private fun saveAndDisplayValues(mX: String?, mY: String?, mZ: String?) {
//
//        var fileWriter: FileWriter? = null
//        try {
//            fileWriter = FileWriter("excel.csv")
//
//            fileWriter.append(CSV_HEADER)
//            fileWriter.append('\n')
//
//            for (myValue in myValues) {
//                fileWriter.append(myValue.valueOfX)
//                fileWriter.append(',')
//                fileWriter.append(myValue.valueOfY)
//                fileWriter.append(',')
//                fileWriter.append(myValue.valueOfZ)
//                fileWriter.append('\n')
//            }
//
//            println("Write CSV successfully!")
//        } catch (e: Exception) {
//            println("Writing CSV error!")
//            e.printStackTrace()
//        } finally {
//            try {
//                fileWriter!!.flush()
//                fileWriter.close()
//            } catch (e: IOException) {
//                println("Flushing/closing error!")
//                e.printStackTrace()
//            }
//        }
//
//    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onStop() {
        super.onStop()
        mSensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        mSensorManager!!.registerListener(
            this,
            mSensorManager?.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager!!.unregisterListener(this)

    }
}
