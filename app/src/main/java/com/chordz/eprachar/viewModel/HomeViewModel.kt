package com.chordz.eprachar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chordz.eprachar.data.ElectionDataHolder
import com.chordz.eprachar.data.MainRepository
import com.chordz.eprachar.data.remote.NetworkState
import com.chordz.eprachar.data.response.ElectionMessageResponse
import kotlinx.coroutines.launch

class HomeViewModel (val repository: MainRepository) : ViewModel() {

    var electionmsgLiveData = MutableLiveData<ElectionMessageResponse>()
        get() = field


    fun getMsgContactNo(
        a_contactno:String
    ) {
        ElectionDataHolder.adminContactNumber = a_contactno
        viewModelScope.launch {
            when (val response = repository.getElectionDetailsMsgByContact(a_contactno)) {
                is NetworkState.Success -> {
                    electionmsgLiveData.postValue(response.data!!)
                }

                is NetworkState.Error -> {
                    if (response.response.code() == 401) {
//                      .postValue(NetworkState.Error)
                    } else {
//                    .postValue(NetworkState.Error)
                    }
                }
            }
        }
    }

//    fun getMsgById(
//        a_id:Int
//    ) {
//        viewModelScope.launch {
//            when (val response = repository.getElectionDetailsMsg(a_id)) {
//                is NetworkState.Success -> {
//                    electionmsgLiveData.postValue(response.data!!)
//                }
//
//                is NetworkState.Error -> {
//                    if (response.response.code() == 401) {
////                      .postValue(NetworkState.Error)
//                    } else {
////                    .postValue(NetworkState.Error)
//                    }
//                }
//            }
//        }
//    }
}