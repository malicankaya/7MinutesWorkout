package com.malicankaya.a7minutesworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        //1
        val abdominalCrunch = ExerciseModel(
            1,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )
        exerciseList.add(abdominalCrunch)

        //2
        val highKneesRunningInPlace = ExerciseModel(
            2,
            "High Knees Running in Place",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )
        exerciseList.add(highKneesRunningInPlace)
        //3
        val jumpingJacks = ExerciseModel(
            3,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )
        exerciseList.add(jumpingJacks)
        //4
        val lunge = ExerciseModel(
            4,
            "Lunge",
            R.drawable.ic_lunge,
            false,
            false
        )
        exerciseList.add(lunge)
        //5
        val plank = ExerciseModel(
            5,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )
        exerciseList.add(plank)
        //6
        val pushUp = ExerciseModel(
            6,
            "Push Up",
            R.drawable.ic_push_up,
            false,
            false
        )
        exerciseList.add(pushUp)
        //7
        val pushUpAndRotation = ExerciseModel(
            7,
            "Push Up and Rotation",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )
        exerciseList.add(pushUpAndRotation)
        //8
        val sidePlank = ExerciseModel(
            8,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false
        )
        exerciseList.add(sidePlank)
        //9
        val squat = ExerciseModel(
            9,
            "Squat",
            R.drawable.ic_squat,
            false,
            false
        )
        exerciseList.add(squat)
        //10
        val stepUpOntoChair = ExerciseModel(
            10,
            "Step Up onto Chair",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        exerciseList.add(stepUpOntoChair)
        //11
        val tricepsDipOnChair = ExerciseModel(
            11,
            "Triceps Dip on Chair",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )
        exerciseList.add(tricepsDipOnChair)
        //12
        val wallSit = ExerciseModel(
            12,
            "Wall Sit",
            R.drawable.ic_wall_sit,
            false,
            false
        )
        exerciseList.add(wallSit)

        return exerciseList
    }
}