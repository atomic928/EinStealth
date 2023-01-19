package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentMainBinding
import com.example.hideandseek.databinding.FragmentWatchBinding
import com.example.hideandseek.ui.viewmodel.WatchFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchFragment: Fragment() {
    private var _binding: FragmentWatchBinding? = null
    private val viewModel: WatchFragmentViewModel by viewModels()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        // Map
        val ivMap: ImageView = binding.ivMap

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
                        }
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

        // Mapに画像をセット
        viewModel.map.observe(viewLifecycleOwner) {
            ivMap.setImageBitmap(it)
        }

        return root
    }
}