package com.puntogris.posture.ui.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.utils.isDarkThemeOn

abstract class BaseBottomSheetFragment<T: ViewDataBinding>(@LayoutRes private val layout: Int, private val isDraggable: Boolean): BottomSheetDialogFragment() {

    private var _binding : T? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            window?.let {
                behavior.isDraggable = isDraggable
                it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                WindowInsetsControllerCompat(it, it.decorView).isAppearanceLightStatusBars = !isDarkThemeOn()
            }
            behavior.skipCollapsed = true

            setOnShowListener {
                findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let { layout->
                    setupFullHeight(layout)
                }
            }
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preInitializeViews()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layout, container, false)
        initializeViews()

        return binding.root
    }

    open fun initializeViews() {}

    open fun preInitializeViews() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}