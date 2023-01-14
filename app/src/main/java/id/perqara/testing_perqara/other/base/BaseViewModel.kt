package id.perqara.testing_perqara.other.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.perqara.testing_perqara.other.wrapper.EventWrapper

open class BaseViewModel: ViewModel() {
    val eventLiveData = MutableLiveData<EventWrapper>()
}