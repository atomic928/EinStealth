package com.example.hideandseek.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BeTrappedFragmentViewModel: ViewModel() {
    lateinit var userLive: LiveData<List<UserData>>
    private val repository = ApiRepository.instance

    fun postTrapRoom(context: Context, isMine: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            Log.d("USER_TRAP", UserRepository(context).nowUser.toString())
            val nowUser = UserRepository(context).nowUser
            val trap = TrapData(0, nowUser.latitude, nowUser.longitude, nowUser.altitude, isMine)
            TrapRepository(context).insert(trap)
        }
    }

    private val _skillTime = MutableLiveData<String>()
    val skillTime: LiveData<String> = _skillTime

    fun setSkillTime(context: Context) {
        val nowUser = UserRepository(context).nowUser
        _skillTime.value = nowUser.relativeTime
    }

    fun setSkillTimeInit(skillTime: String) {
        _skillTime.value = skillTime
    }

    private val _trapTime = MutableLiveData<String>()
    val trapTime: LiveData<String> = _trapTime

    fun setTrapTime(trapTime: String) {
        _trapTime.value = trapTime
    }

    fun setUserLive(context: Context) {
        userLive = UserRepository(context).allUsers.asLiveData()
    }

    private val _limitTime = MutableLiveData<String>()
    val limitTime: LiveData<String> = _limitTime

    fun setLimitTime(limitTime: String) {
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

    private val _isOverTrapTime = MutableLiveData<Boolean>()
    val isOverTrapTime: LiveData<Boolean> = _isOverTrapTime

    fun compareTrapTime(relativeTime: String, trapTime: String) {
        Log.d("CompareTrapTime", "relative: $relativeTime, trap: $trapTime")
        if (relativeTime.substring(6, 8) == trapTime.substring(6, 8)) {
            _isOverTrapTime.value = relativeTime != trapTime
        }
    }

    fun howProgressSkillTime(relativeTime: String, skillTime: String): Int {
        Log.d("HowProgress", ((60+relativeTime.substring(6).toInt()-skillTime.substring(6).toInt())%60).toString())
        if (relativeTime.substring(6).toInt() < skillTime.substring(6).toInt()) {
            return (60+relativeTime.substring(6).toInt()-skillTime.substring(6).toInt())%60
        } else {
            return relativeTime.substring(6).toInt()-skillTime.substring(6).toInt()
        }
    }

    fun howProgressTrapTime(relativeTime: String, trapTime: String): Int {
        Log.d("HowProgressTrap", ((60+relativeTime.substring(6).toInt()-trapTime.substring(6).toInt())%60).toString())
        if (relativeTime.substring(6).toInt() < trapTime.substring(6).toInt()) {
            return (60+relativeTime.substring(6).toInt()-trapTime.substring(6).toInt())%60
        } else {
            return relativeTime.substring(6).toInt()-trapTime.substring(6).toInt()
        }
    }

    fun setIsOverSkillTime(p0: Boolean) {
        _isOverSkillTime.value = p0
    }

    fun postTrapSpacetime(context: Context) {
        val nowUser = UserRepository(context).nowUser
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = PostData.PostSpacetime(nowUser.relativeTime.substring(0, 7)+ "0", nowUser.latitude, nowUser.longitude, nowUser.altitude, 1)
                val response = repository.postSpacetime(request)
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
}