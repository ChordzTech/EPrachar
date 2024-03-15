package com.chordz.eprachar.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chordz.eprachar.data.ElectionDataHolder
import com.chordz.eprachar.data.MainRepository
import com.chordz.eprachar.data.remote.NetworkState
import com.chordz.eprachar.data.response.ElectionMessageResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeViewModel(val repository: MainRepository) : ViewModel() {

    var electionmsgLiveData = MutableLiveData<ElectionMessageResponse>()
        get() = field


    fun getMsgContactNo(
        context: Context,
        a_contactno: String
    ) {
        ElectionDataHolder.adminContactNumber = a_contactno
        viewModelScope.launch {
            when (val response = repository.getElectionDetailsMsgByContact(a_contactno)) {
                is NetworkState.Success -> {
                    if (response.data.code == 200) {
                        ElectionDataHolder.ImageUrl = ElectionDataHolder.BASE_URL + response.data?.data!![0]!!.aImage!!
                        Glide.with(context).asBitmap()
                            .load(
                                ElectionDataHolder.BASE_URL + response.data?.data!![0]!!.aImage!!
                            )
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    ElectionDataHolder.messageBitmap = resource
                                    Toast.makeText(
                                        context,
                                        "Message Downloaded",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }
                            })
                        electionmsgLiveData.postValue(response.data!!)
                    }else{
                        Toast.makeText(
                            context,
                            "Message Downloaded Failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
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