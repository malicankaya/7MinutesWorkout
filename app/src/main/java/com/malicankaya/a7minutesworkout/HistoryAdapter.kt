package com.malicankaya.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malicankaya.a7minutesworkout.databinding.HistoryRowBinding

class HistoryAdapter(
    private val historyList: List<HistoryEntity>,
    private val deleteRecord: (date: String) -> Unit
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var ll = binding.llHistory
        var date = binding.tvWorkoutDate
        var position = binding.tvPosition
        var delete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HistoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = historyList[position].date
        holder.position.text = "#${(position + 1)}"

        if (position % 2 == 0) {
            holder.ll.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.ll.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.bayaLightGrey
                )
            )
        }

        holder.delete.setOnClickListener {
            deleteRecord.invoke(holder.date.toString())
        }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}