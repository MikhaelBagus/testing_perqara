package id.perqara.testing_perqara.other.base

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.perqara.testing_perqara.other.util.SingleLiveEvent
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    val eventLiveData = MutableLiveData<EventWrapper>()

    val disposables = CompositeDisposable()
    val event = SingleLiveEvent<Events>()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

data class Events(val isLoading: Boolean = false, val isSuccess: Boolean? = false, val message: String? = null)