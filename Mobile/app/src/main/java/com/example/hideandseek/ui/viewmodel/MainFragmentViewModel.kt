package com.example.hideandseek.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.hideandseek.data.datasource.local.*
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

class MainFragmentViewModel: ViewModel() {
    lateinit var allLocationsLive: LiveData<List<LocationData>>
    lateinit var allTrapsLive: LiveData<List<TrapData>>
    lateinit var userLive: LiveData<List<UserData>>
    private val repository = ApiRepository.instance

    fun setAllLocationsLive(context: Context) {
        allLocationsLive = LocationRepository(context).allLocations.asLiveData()
    }

    fun setAllTrapsLive(context: Context) {
        allTrapsLive = TrapRepository(context).allTraps.asLiveData()
    }

    fun postTrapRoom(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            Log.d("USER_TRAP", UserRepository(context).nowUser.toString())
            val nowUser = UserRepository(context).nowUser
            val trap = TrapData(0, nowUser.latitude, nowUser.longitude, nowUser.altitude, 0)
            TrapRepository(context).insert(trap)
            postTrapSpacetime(nowUser.relativeTime, nowUser.latitude, nowUser.longitude, nowUser.altitude)
        }
    }

    fun setUserLive(context: Context) {
        userLive = UserRepository(context).allUsers.asLiveData()
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
        if (relativeTime.substring(6, 7) == skillTime.substring(6, 7)) {
            _isOverSkillTime.value = relativeTime.substring(3, 5).toInt() > skillTime.substring(3, 5).toInt()
        }
    }

    fun howProgressSkillTime(relativeTime: String, skillTime: String): Int {
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

    fun getTest() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getTest()
                if (response.isSuccessful) {
                    Log.d("GETTEST", "${response}\n${response.body()}")
                } else {
                    Log.d("GETTEST", "$response")
                }
            } catch (e: java.lang.Exception){
                Log.d("GETTEST", "$e")
            }
        }
    }

    fun postStatus(id: Int, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.postStatus(id, status)
                if (response.isSuccessful) {
                    Log.d("GETTEST", "${response}\n${response.body()}")
                } else {
                    Log.d("GETTEST", "$response")
                }
            } catch (e: java.lang.Exception){
                Log.d("GETTEST", "$e")
            }
        }
    }

    fun getSpacetime(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getSpacetime(time)
                if (response.isSuccessful) {
                    Log.d("GETTEST", "${response}\n${response.body()}")
                } else {
                    Log.d("GETTEST", "$response")
                }
            } catch (e: java.lang.Exception){
                Log.d("GETTEST", "$e")
            }
        }
    }

    private fun postTrapSpacetime(relativeTime: String, latitude: Double, longitude: Double, altitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = PostData.PostSpacetime(relativeTime.substring(0, 7)+ "0", latitude, longitude, altitude, 1)
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

    suspend fun fetchMap(url: String): Bitmap {
        return MapRepository().fetchMap(url)
    }
}

