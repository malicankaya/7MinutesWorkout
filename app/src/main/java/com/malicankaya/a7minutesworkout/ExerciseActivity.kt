package com.malicankaya.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.malicankaya.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    var binding: ActivityExerciseBinding? = null
    private var countDownTimer: CountDownTimer? = null
    private var timerProgressSec = 0
    private var timerSec: Long = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            countDownTimer?.cancel()
            onBackPressed()
        }

        setTimer()

    }

    private fun setTimer() {
        if (countDownTimer != null){
            countDownTimer?.cancel()
            timerProgressSec = 0
        }
        setTimerProgressBar()
    }

    private fun setTimerProgressBar() {
        binding?.progressBar?.progress = timerSec.toInt()
        binding?.tvCount?.text = timerSec.toString()

        countDownTimer = object : CountDownTimer(timerSec * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerProgressSec++
                binding?.progressBar?.progress = timerSec.toInt() - timerProgressSec
                binding?.tvCount?.text = (timerSec.toInt() - timerProgressSec).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"Now, we will start the exercise.",
                Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}