package com.chordz.eprachar.data

import com.chordz.eprachar.data.remote.NetworkState
import com.chordz.eprachar.data.remote.RetroFitService
import com.chordz.eprachar.data.response.ElectionMessageResponse

class MainRepository constructor(private val retroFitService: RetroFitService) {

    suspend fun getElectionDetailsMsgByContact(a_contactno:String): NetworkState<ElectionMessageResponse> {
        val response = retroFitService.getdetailsListByContact(a_contactno)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

//    suspend fun getElectionDetailsMsg(a_id:Int): NetworkState<ElectionMessageResponse> {
//        val response = retroFitService.getdetailsList(a_id)
//        return if (response.isSuccessful) {
//            val responseBody = response.body()
//            if (responseBody != null) {
//                NetworkState.Success(responseBody)
//            } else {
//                NetworkState.Error(response)
//            }
//        } else {
//            NetworkState.Error(response)
//        }
//    }
}