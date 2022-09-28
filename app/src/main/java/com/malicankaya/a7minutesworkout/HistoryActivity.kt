package com.malicankaya.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.malicankaya.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistory)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()

        lifecycleScope.launch {
            dao.getAllHistoryData().collect {
                listHistoryData(dao,it)
            }
        }
    }

    private fun listHistoryData(historyDao: HistoryDao, historyList: List<HistoryEntity>) {
        val adapter = HistoryAdapter(historyList) {
            deleteRecord(it, historyDao)
        }
        if(historyList.isEmpty()){
            binding?.tvNoRecords?.visibility = View.VISIBLE
            binding?.rvHistory?.visibility = View.INVISIBLE
        }else{
            binding?.tvNoRecords?.visibility = View.INVISIBLE
            binding?.rvHistory?.visibility = View.VISIBLE
        }
        binding?.rvHistory?.adapter = adapter
    }

    private fun deleteRecord(date: String, historyDao: HistoryDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(R.drawable.ic_alert)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                historyDao.delete(HistoryEntity(date))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}