package id.perqara.testing_perqara.other.wrapper

open class EventWrapper {
    data class OnToast(val message: String) : EventWrapper()
    object OnLoadingShow : EventWrapper()
    object OnLoadingDissapear : EventWrapper()
    object OnPageFinished : EventWrapper()
    data class OnNetworkError(val page: String, val api: String) : EventWrapper()
    object OnFailed : EventWrapper()
    object OnSuccess : EventWrapper()
    object OnMultipleCallLoadingShow : EventWrapper()
    object OnMultipleCallLoadingDissapear : EventWrapper()
}