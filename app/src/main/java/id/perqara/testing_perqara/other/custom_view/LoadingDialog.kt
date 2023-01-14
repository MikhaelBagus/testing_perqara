package id.perqara.testing_perqara.other.custom_view

import android.app.Dialog
import android.content.Context
import android.view.Window
import id.perqara.testing_perqara.R

class LoadingDialog(context: Context){
    private val dialog by lazy {
        Dialog(context).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.custom_dialog_loading)
            window?.setBackgroundDrawableResource(R.color.colorTransparent)
            dismiss()
        }
    }

    fun showDialog(){
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}