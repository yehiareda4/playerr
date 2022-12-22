package com.aait.player

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var player: MediaPlayer
    var sensorManager: SensorManager? = null
    var accelerometerSensor: Sensor? = null
    var accelerometerPresent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var resId = resources.getIdentifier(
            R.raw.adu.toString(),
            "raw", packageName
        )
        player = MediaPlayer()
        player = MediaPlayer.create(this, resId)
        player.start()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList: List<Sensor> = sensorManager!!.getSensorList(Sensor.TYPE_ACCELEROMETER)
        if (sensorList.size > 0) {
            accelerometerPresent = true
            accelerometerSensor = sensorList[0]
        } else {
            accelerometerPresent = false
        }

    }

    override fun onResume() {
        super.onResume()
        if (accelerometerPresent) {
            sensorManager!!.registerListener(
                accelerometerListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
        if (accelerometerPresent) {
            sensorManager?.unregisterListener(accelerometerListener)
        }
    }

    private val accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {
            // TODO Auto-generated method stub
        }

        override fun onSensorChanged(arg0: SensorEvent) {
            // TODO Auto-generated method stub
            val z_value = arg0.values[2]
            if (z_value >= 0) {
                // Current condition is "FACE UP" \\
                // do your stuff according to this\\
                resumeStream()
            } else {
                // Current condition is "FACE DOWN" \\
                // do your stuff according to this\\
                pauseStream()
            }
        }
    }

    fun pauseStream() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    fun resumeStream() {
        if (!player.isPlaying) {
            player.start()
        }
    }


}