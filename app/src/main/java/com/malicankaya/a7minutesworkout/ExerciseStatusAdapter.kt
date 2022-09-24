package com.malicankaya.a7minutesworkout

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malicankaya.a7minutesworkout.databinding.CustomTextviewExerciseStatusBinding

class ExerciseStatusAdapter(val exerciseList: ArrayList<ExerciseModel>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: CustomTextviewExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CustomTextviewExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.tvItem.text = exercise.getId().toString()

        when{
            exercise.getIsComplated() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.circular_color_accent_background)
            }
            exercise.getIsSelected() ->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_status_active_border)
            }
        }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}