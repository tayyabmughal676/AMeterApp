package com.example.ameterapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

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

    var filename = "text.txt"
    var filepath = "MyFileDir"
    var filecontent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        //      Initialized Get Intent Data
        val userExerciseName = intent!!.getStringExtra("UserMessage")
//        Toast.makeText(applicationContext, "$userExerciseName", Toast.LENGTH_SHORT).show()

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

        //x value
        val mMap = HashMap<String, String>()
        mMap["X"] = mX
        mMap["Y"] = mY
        mMap["Z"] = mZ

        filecontent = mMap.toString()
        val myExternalFile = File(getExternalFilesDir(filepath), filename)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myExternalFile)
            fos.write(filecontent.toByteArray())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //    Submit Exercise Data to CSV File
    private fun submitExerciseData() {
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onStop() {
        super.onStop()
        mSensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
                this,
                mSensorManager?.getDefaultSensor(
                        Sensor.TYPE_ACCELEROMETER
                ),
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager!!.unregisterListener(this)

    }
}
