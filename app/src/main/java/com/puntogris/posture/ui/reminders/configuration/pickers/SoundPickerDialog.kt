package com.puntogris.posture.ui.reminders.configuration.pickers

import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.utils.ToneItem

class SoundPickerDialog(
    private val currentSound: String,
    private val onPickedAction: (ToneItem) -> Unit
) : DialogFragment() {

    private var mediaPlayer: MediaPlayer? = null
    private var selectedPosition = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tones = listRingTones()
        val stringTones = tones.map { it.title }.toTypedArray()
        val matchLastPosition = listRingTones().indexOfFirst { it.uri == currentSound }
        val lastPosition = if (matchLastPosition == -1) 0 else matchLastPosition

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sound_picker_title)
            .setPositiveButton(R.string.action_done) { _, _ ->
                onPickedAction(tones[selectedPosition])
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .setSingleChoiceItems(stringTones, lastPosition) { _, position ->
                selectedPosition = position
                playSound(tones[position].uri)
            }
            .create()
    }

    private fun playSound(uri: String) {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(uri))
        mediaPlayer?.start()
    }

    private fun listRingTones(): ArrayList<ToneItem> {
        val toneItems = arrayListOf(
            ToneItem(getString(R.string.disabled), "")
        )
        RingtoneManager(requireContext()).apply {
            setType(RingtoneManager.TYPE_NOTIFICATION)
        }.cursor?.let {
            while (it.moveToNext()) {
                toneItems.add(ToneItem.from(it))
            }
        }
        return toneItems
    }

    override fun onDismiss(dialog: DialogInterface) {
        mediaPlayer?.stop()
        super.onDismiss(dialog)
    }
}
