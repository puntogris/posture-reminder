package com.puntogris.posture.data.local

import android.content.Context
import com.puntogris.posture.R
import com.puntogris.posture.model.Exercise
import com.puntogris.posture.model.ItemData
import com.puntogris.posture.model.ReminderUi

class LocalDataSource {

    val exercisesList = listOf(
        Exercise(R.string.brugger_exercise_title,
            R.string.brugger_exercise_summary,
            1,
            R.color.card1,
            R.drawable.ic_brugger_exercise,
            R.array.brugger_exercise_steps
        ),
        Exercise(
            R.string.back_extensions_exercise_title,
            R.string.back_extension_exercise_summary,
            45, R.color.card2,
            R.drawable.ic_back_extension_exercise,
            R.array.back_extension_exercise_steps
        ),
        Exercise(R.string.chin_tuck_exercise_title,
            R.string.chin_tuck_exercise_summary,
            45,
            R.color.card3,
            R.drawable.ic_chin_tuck_exercise,
            R.array.chin_tuck_exercise_steps
        ),
        Exercise(
            R.string.side_bend_exercise_title,
            R.string.side_bend_exercise_summary,
            45,
            R.color.card4,
            R.drawable.ic_side_bend_exercise,
            R.array.side_bend_exercise_steps
        )
    )

}