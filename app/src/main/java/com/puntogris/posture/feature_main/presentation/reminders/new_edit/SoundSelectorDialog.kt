package com.puntogris.posture.feature_main.presentation.reminders.new_edit

import android.app.Dialog
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.feature_main.presentation.util.ToneItem
import com.puntogris.posture.common.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.common.utils.constants.Constants.SOUND_PICKER_KEY
import java.util.*

class SoundSelectorDialog : DialogFragment() {

    private var mediaPlayer: MediaPlayer? = null
    private val args: SoundSelectorDialogArgs by navArgs()
    private var selectedPosition = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tones = listRingTones()
        val stringTones = tones.map { it.title }.toTypedArray()
        val matchLastPosition = listRingTones().indexOfFirst { it.uri == args.savedSound }
        val lastPosition = if (matchLastPosition == -1) 0 else matchLastPosition

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setTitle(R.string.sound_picker_title)
            .setPositiveButton(R.string.action_done) { _, _ ->
                setFragmentResult(SOUND_PICKER_KEY, bundleOf(DATA_KEY to tones[selectedPosition]))
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .setSingleChoiceItems(stringTones, lastPosition) { _, position ->
                selectedPosition = position
                mediaPlayer?.stop()
                mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(tones[position].uri))
                mediaPlayer?.start()
            }
            .create()
    }

    private fun listRingTones(): ArrayList<ToneItem> {
        val toneItems = ArrayList<ToneItem>()
        toneItems.add(ToneItem(getString(R.string.disabled), ""))

        val manager = RingtoneManager(requireContext())
        manager.setType(RingtoneManager.TYPE_NOTIFICATION)

        manager.cursor?.let {
            while (it.moveToNext()) {
                toneItems.add(ToneItem.from(it))
            }
        }
        return toneItems
    }
}