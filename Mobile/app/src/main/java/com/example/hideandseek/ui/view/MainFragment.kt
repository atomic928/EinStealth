package com.example.hideandseek.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.databinding.FragmentMainBinding
import com.example.hideandseek.ui.viewmodel.MainFragmentViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.URL
import kotlin.math.log

class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val viewModel: MainFragmentViewModel by viewModels()

    private var trapNumber = 0

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        // Viewの取得
        // 時間表示の場所
        val ivTime:           ImageView = binding.ivTime
        val tvNow:            TextView  = binding.tvNow
        val tvLimit:          TextView  = binding.tvLimit
        val tvRelativeTime:   TextView  = binding.tvRelativeTime
        val tvLimitTime:      TextView  = binding.tvLimitTime
        // Map
        val ivMap:            ImageView = binding.ivMap
        // 捕まったボタン
        val btCaptureOn:      ImageView = binding.btCaptureOn
        val btCaptureOff:     ImageView = binding.btCaptureOff

        fun changeBtCaptureVisible(isOn: Boolean) {
            if (isOn) {
                btCaptureOn.visibility  = View.VISIBLE
                btCaptureOff.visibility = View.INVISIBLE
            } else {
                btCaptureOn.visibility  = View.INVISIBLE
                btCaptureOff.visibility = View.VISIBLE
            }
        }

        // スキルボタン
        val btSkillOn:        ImageView   = binding.btSkillOn
        val btSkillOff:       ImageView   = binding.btSkillOff
        val progressSkill:    ProgressBar = binding.progressSkill

        fun changeBtSkillVisible(isOn: Boolean) {
            if (isOn) {
                btSkillOn.visibility     = View.VISIBLE
                btSkillOff.visibility    = View.INVISIBLE
                progressSkill.visibility = View.INVISIBLE
            } else {
                btSkillOn.visibility     = View.INVISIBLE
                btSkillOff.visibility    = View.VISIBLE
                progressSkill.visibility = View.VISIBLE

                progressSkill.max = 60
            }
        }

        // 捕まったか確認するダイアログ
        val dialogCapture:    ImageView = binding.dialogCapture
        val ivDemonCapture:   ImageView = binding.dialogCaptureDemon
        val btCaptureYes:     ImageView = binding.btCaptureYes
        val btCaptureNo:      ImageView = binding.btCaptureNo

        fun changeCaptureDialogVisible(visibility: Int) {
            dialogCapture.visibility  = visibility
            ivDemonCapture.visibility = visibility
            btCaptureYes.visibility   = visibility
            btCaptureNo.visibility    = visibility
        }

        // 捕まった後のダイアログ
        val dialogCaptured:   ImageView = binding.dialogCaptured
        val ivDemonCaptured:  ImageView = binding.dialogCapturedDemon
        val ivNormalCaptured: ImageView = binding.dialogCapturedNormal
        val metalRod:         ImageView = binding.metalRod
        val btCapturedClose:  ImageView = binding.capturedClose

        fun changeAfterCaptureDialogVisible(visibility: Int) {
            dialogCaptured.visibility   = visibility
            ivDemonCaptured.visibility  = visibility
            ivNormalCaptured.visibility = visibility
            metalRod.visibility         = visibility
            btCapturedClose.visibility  = visibility
        }

        // 観戦中
        val ivWatching:       ImageView = binding.ivWatching

        // 捕まってステータスが捕まったになったら観戦モードになる
        fun changeStatusCaptured() {
            ivWatching.visibility   = View.VISIBLE
            changeBtCaptureVisible(false)
            btSkillOff.visibility   = View.VISIBLE
            btSkillOn.visibility    = View.INVISIBLE
        }

        // User normal
        val user1Normal:      ImageView = binding.user1Normal
        val user2Normal:      ImageView = binding.user2Normal
        val user3Normal:      ImageView = binding.user3Normal
        // User demon
        val user4Demon:       ImageView = binding.user4Demon
        // User captured
        val user1Captured:    ImageView = binding.user1Captured
        // 罠にかかったとき
        val ivEye:            ImageView = binding.ivEye
        val tvTrap:           TextView  = binding.tvTrap
        val trapDialogText:   ImageView = binding.textOniTrap
        val trapDialogDemon:  ImageView = binding.ivOniTrap
        // クリアしたとき
        val dialogClear:      ImageView = binding.dialogClear
        val dialogClearUser1: ImageView = binding.dialogClearUser1
        val btClearClose:     ImageView = binding.clearClose

        fun changeClearDialogVisible(visibility: Int) {
            dialogClear.visibility      = visibility
            dialogClearUser1.visibility = visibility
            btClearClose.visibility     = visibility
        }

        // Result画面
        val resultBack:       ImageView = binding.resultBack
        val tvResult:         TextView  = binding.tvResult
        val tvWinner:         TextView  = binding.tvWinner
        val tvLoser:          TextView  = binding.tvLoser
        val winnerUser1:      ImageView = binding.winnerUser1
        val winnerUser2:      ImageView = binding.winnerUser2
        val loserUser3:       ImageView = binding.loserUser3
        val loserUser4:       ImageView = binding.loserUser4
        val btResultClose:    ImageView = binding.btResultClose

        fun changeResultDialogVisible(visibility: Int) {
            resultBack.visibility    = visibility
            tvResult.visibility      = visibility
            tvWinner.visibility      = visibility
            tvLoser.visibility       = visibility
            winnerUser1.visibility   = visibility
            winnerUser2.visibility   = visibility
            loserUser3.visibility    = visibility
            loserUser4.visibility    = visibility
            btResultClose.visibility = visibility
        }

        fun changeOtherResultDialog(visibility: Int) {
            ivTime.visibility         = visibility
            tvNow.visibility          = visibility
            tvLimit.visibility        = visibility
            tvRelativeTime.visibility = visibility
            tvLimitTime.visibility    = visibility
            user1Normal.visibility    = visibility
            user2Normal.visibility    = visibility
            user3Normal.visibility    = visibility
            user4Demon.visibility     = visibility
            user1Captured.visibility  = visibility
            btSkillOn.visibility      = visibility
            btSkillOff.visibility     = visibility
            progressSkill.visibility  = visibility

            dialogCapture.visibility  = visibility
            ivDemonCapture.visibility = visibility
            btCaptureYes.visibility   = visibility
            btCaptureNo.visibility    = visibility

            btCaptureOn.visibility      = visibility
            btCaptureOff.visibility     = visibility

            ivEye.visibility           = visibility
            tvTrap.visibility          = visibility
            trapDialogText.visibility  = visibility
            trapDialogDemon.visibility = visibility

            dialogClear.visibility      = visibility
            dialogClearUser1.visibility = visibility
            btClearClose.visibility     = visibility
        }


        // データベースからデータを持ってくる
        context?.let {
            viewModel.setAllLocationsLive(it)
            viewModel.setUserLive(it)
            viewModel.setAllTrapsLive(it)
        }

        // 自分の情報の表示
        viewModel.userLive.observe(viewLifecycleOwner) { userLive ->
            Log.d("UserLive", userLive.toString())
            if (userLive.isNotEmpty()) {
                viewModel.setLimitTime(userLive[0].relativeTime)
                tvRelativeTime.text = userLive[userLive.size-1].relativeTime
                // 制限時間になったかどうかの判定
                viewModel.limitTime.observe(viewLifecycleOwner) { limitTime ->
                    viewModel.compareTime(userLive[userLive.size-1].relativeTime, limitTime)
                }

                // 自分の位置情報のurl
                val iconUrlHide = "https://onl.bz/dcMZVEa"
                var url = "https://maps.googleapis.com/maps/api/staticmap" +
                        "?center=${userLive[userLive.size-1].latitude},${userLive[userLive.size-1].longitude}" +
                        "&size=310x640&scale=1" +
                        "&zoom=18" +
                        "&key=AIzaSyA-cfLegBoleKaT2TbU5R4K1uRkzBR6vUQ" +
                        "&markers=icon:" + iconUrlHide + "|${userLive[userLive.size-1].latitude},${userLive[userLive.size-1].longitude}"

                // 他人の位置を追加
                viewModel.allLocationsLive.observe(viewLifecycleOwner) { allLocationLive ->
                    Log.d("ALL_Location", allLocationLive.toString())
                    if (allLocationLive.isNotEmpty()) {
                        // ユーザーの位置情報
                        for (i in allLocationLive.indices) {
                            if (allLocationLive[i].objId == 1) {
                                context?.let { context -> viewModel.postTrapRoom(context, 1) }
                            } else {
                                url += "&markers=icon:" + iconUrlHide + "|${allLocationLive[i].latitude},${allLocationLive[i].longitude}"
                            }
                        }
                    }
                }

                // trapの位置情報
                viewModel.allTrapsLive.observe(viewLifecycleOwner) { allTrap ->
                    if (allTrap.isNotEmpty()) {
                        for (i in allTrap.indices) {
                            if (allTrap[i].objId == 0) {
                                url += "&markers=icon:https://onl.bz/FetpS7Y|${allTrap[i].latitude},${allTrap[i].longitude}"
                            }
                            viewModel.checkCaughtTrap(userLive[userLive.size-1], allTrap[i])
                        }
                    }
                }

                // Skill Buttonの Progress Bar
                // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
                // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
                // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
                if (trapNumber > 0) {
                    viewModel.skillTime.observe(viewLifecycleOwner) { skillTime ->
                        viewModel.compareSkillTime(userLive[userLive.size-1].relativeTime,
                            skillTime
                        )
                        progressSkill.progress = viewModel.howProgressSkillTime(userLive[userLive.size-1].relativeTime,
                            skillTime
                        )
                    }
                }

                // URLから画像を取得
                // 相対時間10秒おきに行う
                if (userLive[userLive.size-1].relativeTime.substring(7, 8) == "0") {
                    Log.d("fetchMAP", "Mapが更新されました")
                    coroutineScope.launch {
                        val originalBitmap = viewModel.fetchMap(url)
                        viewModel.setMap(originalBitmap)
                    }
                }
            }
        }

        // Trap情報の監視
        viewModel.allTrapsLive.observe(viewLifecycleOwner) {
            Log.d("TRAP_LIVE", it.toString())
        }

        viewModel.limitTime.observe(viewLifecycleOwner) {
            tvLimitTime.text = it
        }

        viewModel.isOverLimitTime.observe(viewLifecycleOwner) {
            if (it) {
                // クリアダイアログを表示
                changeClearDialogVisible(View.VISIBLE)
            }
        }

        // クリアダイアログの閉じるを押した時
        btClearClose.setOnClickListener {
            // クリアダイアログを非表示
            changeClearDialogVisible(View.INVISIBLE)

            // Resultダイアログの表示
            changeResultDialogVisible(View.VISIBLE)

            // Result以外のものを非表示
            changeOtherResultDialog(View.INVISIBLE)
        }

        // Resultダイアログの閉じるを押した時の処理
        btResultClose.setOnClickListener {
            // Resultダイアログの非表示
            changeResultDialogVisible(View.INVISIBLE)

            // Result以外のものを表示
            changeOtherResultDialog(View.VISIBLE)
        }

        // 捕まったボタンが押された時の処理
        btCaptureOn.setOnClickListener {
            // ボタンを押された状態にする
            changeBtCaptureVisible(false)
            // 捕まったか確認するダイアログが出現
            changeCaptureDialogVisible(View.VISIBLE)
        }

        btCaptureNo.setOnClickListener {
            // ボタンを押されていない状態にする
            changeBtCaptureVisible(true)
            // 捕まったか確認するダイアログが消える
            changeCaptureDialogVisible(View.INVISIBLE)
        }

        btCaptureYes.setOnClickListener {
            // 捕まったか確認するダイアログが消える
            changeCaptureDialogVisible(View.INVISIBLE)

            // 捕まったダイアログが出る
            changeAfterCaptureDialogVisible(View.VISIBLE)
        }

        btCapturedClose.setOnClickListener {
            // 捕まったダイアログが消える
            changeAfterCaptureDialogVisible(View.INVISIBLE)

            // 観戦モードになる
            changeStatusCaptured()

            // ステータスが変わる
            user1Normal.visibility   = View.INVISIBLE
            user1Captured.visibility = View.VISIBLE
        }

        // skillボタンが押された時の処理
        btSkillOn.setOnClickListener {
            // Userの最新情報から位置をとってきて、それを罠の位置とする
            context?.let {
                viewModel.postTrapRoom(it, 0)
                viewModel.postTrapSpacetime(it)
                viewModel.setSkillTime(it)
            }
            viewModel.setIsOverSkillTime(false)
            trapNumber += 1
        }

        viewModel.isOverSkillTime.observe(viewLifecycleOwner) {
            changeBtSkillVisible(it)
        }



        // Mapに画像をセット
        viewModel.map.observe(viewLifecycleOwner) {
            ivMap.setImageBitmap(it)
        }



        return root
    }
}
