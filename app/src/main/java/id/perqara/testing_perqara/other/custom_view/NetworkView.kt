package id.perqara.testing_perqara.other.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import id.perqara.testing_perqara.R

class NetworkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var textRetry: TextView
    lateinit var linearLayout: LinearLayout
    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.custom_view_network, this)
        textRetry = view.findViewById(R.id.txt_retry)
        linearLayout = view.findViewById(R.id.networkLayout)

        this.visibility = View.GONE
    }

    public fun removeView(){
        if(parent == null) return
        (parent as ViewGroup).removeView(this)
    }

    public fun goneView(){
        this.visibility = View.GONE
    }

    public fun visibleView(){
        this.visibility= View.VISIBLE
    }

    public fun setOnRetryListener(listener: OnClickListener){
        textRetry.setOnClickListener(listener)
    }
}