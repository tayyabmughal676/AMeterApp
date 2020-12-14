package com.example.ameterapp.excelFiles

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import com.example.ameterapp.R
import java.util.*

class CalculationActivity : AppCompatActivity(), View.OnClickListener, SensorEventListener {

    val TAG = "CalculationActivity"
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null

    private var updateTimer: Timer? = null
    private var linearAcceleration = FloatArray(3)
    private var velocity: Velocity? = null
    private var handler: Handler? = null

    private lateinit var mBtnStart: Button
    private lateinit var mBtnStop: Button
    private lateinit var mCountryTable: TableLayout
    private lateinit var mValueOfSpeedMPSTV: TextView
    private lateinit var mValueOfSpeedKMPHTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        initSensor()
        mBtnStart = findViewById(R.id.buttonStart)
        mBtnStop = findViewById(R.id.buttonStop)
        mCountryTable = findViewById(R.id.country_table)
        mCountryTable = findViewById(R.id.country_table)
        mValueOfSpeedMPSTV = findViewById(R.id.valueOfSpeedMPSTV)
        mValueOfSpeedKMPHTV = findViewById(R.id.valueOfSpeedKMPHTV)

        mBtnStart.setOnClickListener(this)
        mBtnStop.setOnClickListener(this)
    }

    private fun initSensor() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (mAccelerometer == null) {
            Toast.makeText(this, "Accelerometer sensor not available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun fillTable(values: FloatArray) {
        //Converting to dip unit
        val dip = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1.toFloat(), resources.displayMetrics
        ).toInt()

        //crate row and add the view inside the row and at the end add row into the tableLayout
        val row = TableRow(this)
        val t1 = TextView(this)
        t1.setTextColor(Color.WHITE)
        t1.setBackgroundColor(Color.GRAY)
        val t2 = TextView(this)
        t2.setTextColor(Color.WHITE)
        t2.setBackgroundColor(Color.LTGRAY)
        val t3 = TextView(this)
        t3.setTextColor(Color.WHITE)
        t3.setBackgroundColor(Color.GRAY)
        t1.text = "" + values[0]
        t2.text = "" + values[1]
        t3.text = "" + values[2]
        t1.typeface = null
        t2.typeface = null
        t3.typeface = null
        t1.textSize = 14f
        t2.textSize = 14f
        t3.textSize = 14f
        t1.width = 130 * dip
        t2.width = 130 * dip
        t3.width = 130 * dip
        t1.setPadding(2 * dip, 0, 0, 0)
        row.addView(t1)
        row.addView(t2)
        row.addView(t3)
        mCountryTable.addView(
                row, TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        )
    }

    override fun onClick(v: View) {
        when {
            v === mBtnStart -> {
                startAction()
            }
            v === mBtnStop -> {
                stopAction()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun stopAction() {
        // Handle UI and unRegister Listener and cancel the timer.
        mCountryTable.removeAllViews()
        mValueOfSpeedMPSTV.text = "V = 0 M/s"
        mValueOfSpeedKMPHTV.text = "V = 0 Km/h"

        mSensorManager?.unregisterListener(this)
        displayVelocityValues()
        velocity = null
        handler = null
        updateTimer?.cancel()
    }

    private fun startAction() {
        // Initialize instance and Register the Listener, scheduled the timer.
        mSensorManager?.registerListener(
                this,
                mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
        )
        velocity = Velocity()
        updateTimer = Timer("velocityUpdate")
        handler = Handler(Looper.getMainLooper())
        updateTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                calculateAndUpdate()
            }
        }, 0, 1200)
    }

    private fun displayVelocityValues() {
        val vl: DoubleArray? = velocity?.vlArray
        vl?.let {
            for (i in vl.indices) {
                Log.d(TAG, "V = " + vl[i] + "M/s, " + vl[i] * 3.6 + " Km/h")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateAndUpdate() {
        val vel: Double? = velocity?.getVelocity(linearAcceleration, System.currentTimeMillis())
        val velKmph = vel?.times(3.6)
        handler?.post {
            mValueOfSpeedMPSTV.text = "V = ${vel?.toInt()} M/s"
            mValueOfSpeedKMPHTV.text = "V = ${velKmph?.toInt()} Km/h"

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        linearAcceleration[0] = event.values[0]
        linearAcceleration[1] = event.values[1]
        linearAcceleration[2] = event.values[2]

        // fill data into the tableLayout
        fillTable(linearAcceleration)
    }
}