package id.perqara.testing_perqara.other.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
Extracted from MVVM Google Blueprints Project -
https://github.com/googlesamples/android-architecture/blob/dev-todo-mvvm-live-kotlin/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.kt
 */

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {


    private val pending = AtomicBoolean(false)
    private val observers = mutableSetOf<Observer<T>>()

    private val internalObserver = Observer<T> { t ->
        if (pending.compareAndSet(true, false)) {
            observers.forEach { observer ->
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observers.add(observer as Observer<T>)

        if (!hasObservers()) {
            super.observe(owner, internalObserver)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.clear()
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T>) {
        observers.remove(observer as Observer<T>)
        super.removeObserver(observer)
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    @MainThread
    fun call() {
        value = null
    }


//    private val pending = AtomicBoolean(false)
//
//    @MainThread
//    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
//
//        if (hasActiveObservers()) {
//            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
//        }
//
//        // Observe the internal MutableLiveData
//        super.observe(owner, Observer<T> { t ->
//            if (pending.compareAndSet(true, false)) {
//                observer.onChanged(t)
//            }
//        })
//    }
//
//    @MainThread
//    override fun setValue(t: T?) {
//        pending.set(true)
//        super.setValue(t)
//    }
//
//    /**
//     * Used for cases where T is Void, to make calls cleaner.
//     */
//    @MainThread
//    fun call() {
//        value = null
//    }
//
//    companion object {
//        private const val TAG = "SingleLiveEvent"
//    }
}