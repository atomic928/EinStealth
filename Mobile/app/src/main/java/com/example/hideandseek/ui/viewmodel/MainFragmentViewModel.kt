package com.example.hideandseek.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.*
import com.example.hideandseek.data.datasource.local.*
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class MainFragmentViewModel (
    private val locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val userRepository: UserRepository,
    private val apiRepository: ApiRepository
): ViewModel() {
    val allLocationsLive = locationRepository.allLocations.asLiveData()
    val allTrapsLive = trapRepository.allTraps.asLiveData()
    val userLive = userRepository.allUsers.asLiveData()

    suspend fun getNowUser(): UserData {
        return userRepository.getLatest()
    }

    fun postTrapRoom(isMine: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            Log.d("USER_TRAP", userRepository.getLatest().toString())
            val nowUser = userRepository.getLatest()
            val trap = TrapData(0, nowUser.latitude, nowUser.longitude, nowUser.altitude, isMine)
            trapRepository.insert(trap)
        }
    }

    private val _skillTime = MutableLiveData<String>()
    val skillTime: LiveData<String> = _skillTime

    fun setSkillTime() = viewModelScope.launch{
        withContext(Dispatchers.Main) {
            val nowUser = userRepository.getLatest()
            _skillTime.value = nowUser.relativeTime
        }
    }

    fun setSkillTImeString(skillTime: String) {
        _skillTime.value = skillTime
    }

    private val _limitTime = MutableLiveData<String>()
    val limitTime: LiveData<String> = _limitTime

    // RelativeTime+15分の時間を制限時間とする
    fun setLimitTime(relativeTime: String) {
        var limitTime = ""
        if (relativeTime.substring(3, 5).toInt() < 45) {
            limitTime = relativeTime.substring(0, 3) + (relativeTime.substring(3, 5).toInt()+15).toString() + relativeTime.substring(5)
        } else if (relativeTime.substring(3, 5).toInt() < 55) {
            if (relativeTime.substring(0, 2).toInt() == 23) {
                limitTime = "00:0"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            } else if (relativeTime.substring(0, 2).toInt() >= 9) {
                limitTime = (relativeTime.substring(0, 2).toInt()+1).toString()+":0"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            } else {
                limitTime = "0"+(relativeTime.substring(0, 2).toInt()+1).toString()+":0"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            }
        } else {
            if (relativeTime.substring(0, 2).toInt() == 23) {
                limitTime = "00:"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            } else if (relativeTime.substring(0, 2).toInt() >= 9) {
                limitTime = (relativeTime.substring(0, 2).toInt()+1).toString()+":"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            } else {
                limitTime = "0"+(relativeTime.substring(0, 2).toInt()+1).toString()+":"+((relativeTime.substring(3, 5).toInt()+15)%60).toString() + relativeTime.substring(5)
            }
        }
        _limitTime.value = limitTime
    }

    private val _isOverLimitTime = MutableLiveData<Boolean>()
    val isOverLimitTime: LiveData<Boolean> = _isOverLimitTime

    // 相対時間が制限時間を超えてたらtrueを返す
    fun compareTime(relativeTime: String, limitTime: String) {
        _isOverLimitTime.value = relativeTime.substring(0, 2) == limitTime.substring(0, 2) && relativeTime.substring(3, 5) == limitTime.substring(3, 5) && relativeTime.substring(6) > limitTime.substring(6)
    }

    private val _isOverSkillTime = MutableLiveData<Boolean>()
    val isOverSkillTime: LiveData<Boolean> = _isOverSkillTime

    fun compareSkillTime(relativeTime: String, skillTime: String) {
        Log.d("CompareSkillTime", "relative: $relativeTime, skill: $skillTime")
        if (relativeTime.substring(6, 8) == skillTime.substring(6, 8)) {
            _isOverSkillTime.value = relativeTime != skillTime
        }
    }

    fun checkCaughtTrap(user: UserData, trap: TrapData): Boolean {
        // UserがTrapと一定の距離に来たかどうかを返す
        Log.d("checkCaughtTrap", (abs(user.latitude-trap.latitude) + abs(user.longitude-trap.longitude)).toString())
        // 自分の罠の場合は当たり判定を行わない
        if (trap.objId == 0) {
            return false
        }
        // 緯度・経度1どの違いで約100kmの差
        // よって0.00001の差で1m程度の差になる
        // 今回は0.000001以内、つまり10cm以内に入ったら当たった判定
        return (abs(user.latitude-trap.latitude) < 0.000001 && abs(user.longitude-trap.longitude) < 0.000001)
    }

    fun howProgressSkillTime(relativeTime: String, skillTime: String): Int {
        Log.d("HowProgress", ((60+relativeTime.substring(6).toInt()-skillTime.substring(6).toInt())%60).toString())
        if (relativeTime.substring(6).toInt() < skillTime.substring(6).toInt()) {
            return (60+relativeTime.substring(6).toInt()-skillTime.substring(6).toInt())%60
        } else {
            return relativeTime.substring(6).toInt()-skillTime.substring(6).toInt()
        }
    }

    fun setIsOverSkillTime(p0: Boolean) {
        _isOverSkillTime.value = p0
    }

    private val _map = MutableLiveData<Bitmap>()
    val map: LiveData<Bitmap> = _map

    fun setMap(p0: Bitmap) {
        _map.value = p0
    }

    fun postTrapSpacetime() {
        viewModelScope.launch(Dispatchers.IO) {
            val nowUser = userRepository.getLatest()
            try {
                val request = PostData.PostSpacetime(nowUser.relativeTime.substring(0, 7)+ "0", nowUser.latitude, nowUser.longitude, nowUser.altitude, 1)
                val response = apiRepository.postSpacetime(request)
                if (response.isSuccessful) {
                    Log.d("POSTTEST", "${response}\n${response.body()}")
                } else {
                    Log.d("POSTTEST", "$response")
                }
            } catch (e: java.lang.Exception){
                Log.d("POSTTEST", "$e")
            }
        }
    }

    suspend fun fetchMap(url: String): Bitmap {
        return MapRepositoryImpl().fetchMap(url)
    }
}

class MainFragmentViewModelFactory(
    private val locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val userRepository: UserRepository,
    private val apiRepository: ApiRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(locationRepository, trapRepository, userRepository, apiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


