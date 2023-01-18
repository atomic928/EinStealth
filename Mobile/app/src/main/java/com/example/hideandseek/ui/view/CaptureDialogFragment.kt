package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentCaptureDialogBinding

class CaptureDialogFragment: DialogFragment() {
    private var _binding: FragmentCaptureDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCaptureDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 捕まったか確認するダイアログ
        val btCaptureYes:   ImageView = binding.btCaptureYes
        val btCaptureNo:    ImageView = binding.btCaptureNo

        btCaptureYes.setOnClickListener {
            // TODO: 観戦モードに移動する
            dialog?.dismiss()
        }

        btCaptureNo.setOnClickListener {
            dialog?.dismiss()
        }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.fragment_capture_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setDimAmount(0f)
        return dialog
    }
}