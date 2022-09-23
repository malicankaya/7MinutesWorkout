package com.malicankaya.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.malicankaya.a7minutesworkout.databinding.ActivityExerciseBinding
import androidx.constraintlayout.widget.ConstraintSet
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var binding: ActivityExerciseBinding? = null
    private var readyCountDownTimer: CountDownTimer? = null
    private var readyTimerProgressSec = 0
    private var readyTimerSec: Long = 3

    private var exerciseCountDownTimer: CountDownTimer? = null
    private var exerciseTimerProgressSec = 0
    private var exerciseTimerSec: Long = 3

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

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

        binding?.progressBarReady?.max = readyTimerSec.toInt()
        binding?.exerciseProgressBar?.max = exerciseTimerSec.toInt()

        tts = TextToSpeech(this, this)

        setReadyTimer()

    }

    private fun setReadyTimer() {
        binding?.tvGetReady?.visibility = View.VISIBLE
        binding?.flReady?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE

        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExercise?.visibility = View.INVISIBLE
        binding?.ivExerciseImage?.visibility = View.INVISIBLE

        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()

        if (readyCountDownTimer != null) {
            readyCountDownTimer?.cancel()
            readyTimerProgressSec = 0
        }
        setReadyTimerProgressBar()
    }

    private fun setReadyTimerProgressBar() {
        binding?.progressBarReady?.progress = readyTimerSec.toInt()
        binding?.tvReadyCount?.text = readyTimerSec.toString()

        readyCountDownTimer = object : CountDownTimer(readyTimerSec * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                readyTimerProgressSec++
                binding?.progressBarReady?.progress = readyTimerSec.toInt() - readyTimerProgressSec
                binding?.tvReadyCount?.text =
                    (readyTimerSec.toInt() - readyTimerProgressSec).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseSetTimer()
            }
        }.start()
    }

    private fun exerciseSetTimer() {
        binding?.tvGetReady?.visibility = View.INVISIBLE
        binding?.flReady?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExercise?.visibility = View.VISIBLE
        binding?.ivExerciseImage?.visibility = View.VISIBLE

        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())

        speakOut(exerciseList!![currentExercisePosition].getName())

        if (exerciseCountDownTimer != null) {
            exerciseCountDownTimer?.cancel()
            exerciseTimerProgressSec = 0
        }

        exerciseSetTimerProgressBar()
    }

    private fun exerciseSetTimerProgressBar() {
        binding?.exerciseProgressBar?.progress = exerciseTimerSec.toInt()
        binding?.tvExerciseCount?.text = exerciseTimerSec.toString()

        exerciseCountDownTimer = object : CountDownTimer(exerciseTimerSec * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseTimerProgressSec++
                binding?.exerciseProgressBar?.progress =
                    exerciseTimerSec.toInt() - exerciseTimerProgressSec
                binding?.tvExerciseCount?.text =
                    (exerciseTimerSec.toInt() - exerciseTimerProgressSec).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList!!.size) {
                    setReadyTimer()

                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed 7 Minutes Workout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported.")
            }else{
                Log.e("TTS","Initialization Failed!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (readyCountDownTimer != null) {
            readyCountDownTimer?.cancel()
            readyTimerProgressSec = 0
        }
        if (exerciseCountDownTimer != null) {
            exerciseCountDownTimer?.cancel()
            exerciseTimerProgressSec = 0
        }
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        binding = null
    }


}