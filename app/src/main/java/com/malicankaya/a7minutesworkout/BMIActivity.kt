package com.malicankaya.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.malicankaya.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var binding: ActivityBmiBinding? = null
    private var currentVisibleView: String =
        METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBMI)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarBMI?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnBMICalculate?.setOnClickListener {

            if (!binding?.etMetricHeight?.text.isNullOrEmpty() && !binding?.etWeight?.text.isNullOrEmpty()) {
                val bmi = calculateBMI(
                    (binding?.etMetricHeight?.text.toString().toFloat() / 100).toString(),
                    binding?.etWeight?.text.toString()
                )
                val bmiType = getBMIType(bmi)

                setBMIViewItems(bmi, bmiType)
                binding?.llBMI?.visibility = View.VISIBLE

            } else {
                Toast.makeText(
                    this@BMIActivity,
                    "Please enter valid values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rbUSUnits){
                makeVisibleUSUnitView()
            }else{
                makeVisibleMetricUnitView()
            }
        }
    }

    private fun makeVisibleMetricUnitView(){
        currentVisibleView = METRIC_UNITS_VIEW

        binding?.llUSHeightUnits?.visibility = View.INVISIBLE
        binding?.etMetricHeight?.visibility = View.VISIBLE

        binding?.llBMI?.visibility = View.INVISIBLE

        binding?.etWeight?.hint = "WEIGHT (in kg)"


        binding?.etUSFeet?.text?.clear()
        binding?.etUSInch?.text?.clear()
        binding?.etWeight?.text?.clear()
    }

    private fun makeVisibleUSUnitView(){
        currentVisibleView = US_UNITS_VIEW

        binding?.llUSHeightUnits?.visibility = View.VISIBLE
        binding?.etMetricHeight?.visibility = View.INVISIBLE

        binding?.llBMI?.visibility = View.INVISIBLE

        binding?.etWeight?.hint = "WEIGHT (in pound)"


        binding?.etMetricHeight?.text?.clear()
        binding?.etWeight?.text?.clear()
    }

    private fun calculateBMI(height: String, weight: String): Float {

        return weight.toFloat() / (height.toFloat() * height.toFloat())
    }

    private fun getBMIType(bmi: Float): BMIType {
        val bmiType: BMIType

        if (bmi.compareTo(40f) > 0) {
            bmiType = BMIType(
                "Obese Class ||| (Very Severely obese)",
                "OMG! You are in a very dangerous condition! Act now!"
            )
        } else if (bmi.compareTo(35f) > 0) {
            bmiType = BMIType(
                "Obese Class || (Severely obese)",
                "OMG! You are in a very dangerous condition! Act now!"
            )
        } else if (bmi.compareTo(30f) > 0) {
            bmiType = BMIType(
                "Obese Class | (Moderately obese)",
                "Oops! You really need to take care of your yourself! Workout maybe!"
            )
        } else if (bmi.compareTo(25f) > 0) {
            bmiType = BMIType(
                "Overweight",
                "Oops! You really need to take care of your yourself! Workout maybe!"
            )
        } else if (bmi.compareTo(18.5f) > 0) {
            bmiType = BMIType(
                "Normal",
                "Congratulations! You are in a good shape!"
            )
        } else if (bmi.compareTo(16f) > 0) {
            bmiType = BMIType(
                "Underweight",
                "Oops! You really need to take better care of yourself! Eat more!"
            )
        } else if (bmi.compareTo(15f) > 0) {
            bmiType = BMIType(
                "Severely underweight",
                "Oops!You really need to take better care of yourself! Eat more!"
            )
        } else {
            bmiType = BMIType(
                "Very severely underweight",
                "Oops! You really need to take better care of yourself! Eat more!"
            )
        }

        return bmiType
    }

    private fun setBMIViewItems(bmi: Float, bmiType: BMIType) {
        val bmiString = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiString
        binding?.tvBMIType?.text = bmiType.type
        binding?.tvBMIContent?.text = bmiType.description

    }

    data class BMIType(val type: String, val description: String)
}