package com.puntogris.posture.preferences

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.BUG_REPORT_COLLECTION_NAME
import com.puntogris.posture.utils.Constants.BUG_REPORT_DOCUMENT_KEY

class ReportBugDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setView(R.layout.bug_report_dialog)
                .setTitle(R.string.bug_report_title_pref)
                .setPositiveButton(
                    getString(R.string.ok)
                ) { dialog, _ ->
                    val report = (dialog as Dialog).findViewById<EditText>(R.id.report).text.toString()
                    val valueToDatabase = hashMapOf(BUG_REPORT_DOCUMENT_KEY to report)
                    Firebase.firestore.collection(BUG_REPORT_COLLECTION_NAME).document()
                        .set(valueToDatabase)
                    Toast.makeText(
                        context,
                        getString(R.string.bug_report_sended_toast),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { _, _ ->
                    // User cancelled the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
