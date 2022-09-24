package com.malicankaya.a7minutesworkout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.malicankaya.a7minutesworkout.databinding.ActivityExerciseBinding
import com.malicankaya.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var binding: ActivityExerciseBinding? = null
    private var readyCountDownTimer: CountDownTimer? = null
    private var readyTimerProgressSec = 0
    private var readyTimerSec: Long = 1

    private var exerciseCountDownTimer: CountDownTimer? = null
    private var exerciseTimerProgressSec = 0
    private var exerciseTimerSec: Long = 1

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

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
            dialogCustomForBackButton()
        }

        binding?.progressBarReady?.max = readyTimerSec.toInt()
        binding?.exerciseProgressBar?.max = exerciseTimerSec.toInt()

        tts = TextToSpeech(this, this)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter

        setReadyTimer()

    }

    override fun onBackPressed() {
        dialogCustomForBackButton()
    }

    private fun dialogCustomForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnDialogBackYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        dialogBinding.btnDialogCustomNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setReadyTimer() {

        try {
            val soundUri =
                Uri.parse("android.resource://com.malicankaya.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
            player?.start()

        } catch (e: Exception) {
            Log.e("Player Error", "An error occurred while playing sound")
        }

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

                if(exerciseList!![currentExercisePosition+1].getIsSelected()){
                    exerciseList!![currentExercisePosition+1].setIsSelected(false)
                    exerciseAdapter!!.notifyItemChanged(currentExercisePosition+1)
                }else{
                    exerciseList!![currentExercisePosition+1].setIsSelected(true)
                    exerciseAdapter!!.notifyItemChanged(currentExercisePosition+1)
                }
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

        exerciseList!![currentExercisePosition].setIsSelected(true)
        exerciseAdapter!!.notifyItemChanged(currentExercisePosition)
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
                if (currentExercisePosition < exerciseList!!.size - 1) {
                    exerciseList!![currentExercisePosition ].setIsComplated(true)
                    exerciseAdapter!!.notifyItemChanged(currentExercisePosition)
                    setReadyTimer()

                } else {
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }.start()

    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported.")
            } else {
                Log.e("TTS", "Initialization Failed!")
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

        if (player != null) {
            player?.stop()
            player = null
        }
        binding = null
    }


}