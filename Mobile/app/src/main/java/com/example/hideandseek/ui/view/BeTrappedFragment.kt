package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.hideandseek.databinding.FragmentBeTrappedBinding
import com.example.hideandseek.ui.viewmodel.BeTrappedFragmentViewModel

class BeTrappedFragment: Fragment() {
    private var _binding: FragmentBeTrappedBinding? = null
    private val viewModel: BeTrappedFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeTrappedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        // 時間表示の場所
        val ivTime: ImageView = binding.ivTime
        val tvNow: TextView = binding.tvNow
        val tvLimit: TextView = binding.tvLimit
        val tvRelativeTime: TextView = binding.tvRelativeTime
        val tvLimitTime: TextView = binding.tvLimitTime

        // 捕まったボタン
        val btCaptureOn:      ImageView = binding.btCaptureOn

        // スキルボタン
        val btSkillOn:        ImageView   = binding.btSkillOn
        val btSkillOff:       ImageView   = binding.btSkillOff
        val progressSkill: ProgressBar = binding.progressSkill

        // Trapが解除されるまでのプログレスバー
        val progressTrap:     ProgressBar = binding.progressTrap

        setFragmentResultListener("trapTime") {key, bundle ->
            val result = bundle.getString(key)
            tvLimitTime.text = result
        }

        return root
    }
}