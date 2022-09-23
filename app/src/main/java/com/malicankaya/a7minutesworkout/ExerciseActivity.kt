package com.malicankaya.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.malicankaya.a7minutesworkout.databinding.ActivityExerciseBinding
import androidx.constraintlayout.widget.ConstraintSet




class ExerciseActivity : AppCompatActivity() {
    var binding: ActivityExerciseBinding? = null
    private var readyCountDownTimer: CountDownTimer? = null
    private var readyTimerProgressSec = 0
    private var readyTimerSec: Long = 10

    private var exerciseCountDownTimer: CountDownTimer? = null
    private var exerciseTimerProgressSec = 0
    private var exerciseTimerSec: Long = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        exerciseList = Constants.defaultExerciseList()

        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            readyCountDownTimer?.cancel()
            onBackPressed()
        }

        setTimer()

    }

    private fun setTimer() {
        if (readyCountDownTimer != null){
            readyCountDownTimer?.cancel()
            readyTimerProgressSec = 0
        }
        setTimerProgressBar()
    }

    private fun setTimerProgressBar() {
        binding?.progressBarReady?.progress = readyTimerSec.toInt()
        binding?.tvReadyCount?.text = readyTimerSec.toString()

        readyCountDownTimer = object : CountDownTimer(readyTimerSec * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                readyTimerProgressSec++
                binding?.progressBarReady?.progress = readyTimerSec.toInt() - readyTimerProgressSec
                binding?.tvReadyCount?.text = (readyTimerSec.toInt() - readyTimerProgressSec).toString()
            }
            override fun onFinish() {
                currentExercisePosition++
                exerciseSetTimer()
            }
        }.start()
    }

    private fun exerciseSetTimer() {
        binding?.flReady?.visibility = View.GONE
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding?.root)
        constraintSet.connect(binding?.tvExercise!!.id,ConstraintSet.BOTTOM,binding?.flExercise!!.id,ConstraintSet.TOP,0);
        constraintSet.applyTo(binding?.root)
        binding?.tvExercise?.text = "Exercise name"
        binding?.flExercise?.visibility = View.VISIBLE

        if (exerciseCountDownTimer != null){
            exerciseCountDownTimer?.cancel()
            exerciseTimerProgressSec = 0
        }

        exerciseSetTimerProgressBar()
    }

    private fun exerciseSetTimerProgressBar() {
        binding?.exerciseProgressBar?.progress = exerciseTimerSec.toInt()
        binding?.tvExerciseCount?.text = exerciseTimerSec.toString()

        readyCountDownTimer = object : CountDownTimer(exerciseTimerSec * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseTimerProgressSec++
                binding?.exerciseProgressBar?.progress = exerciseTimerSec.toInt() - exerciseTimerProgressSec
                binding?.tvExerciseCount?.text = (exerciseTimerSec.toInt() - exerciseTimerProgressSec).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"Now, we will start the exercise.",
                    Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}