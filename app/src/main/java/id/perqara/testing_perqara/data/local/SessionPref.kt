package id.perqara.testing_perqara.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SessionPref {
    private var sharedPreferences: SharedPreferences? = null
    private var spEditor: SharedPreferences.Editor? = null
    private val NAME_SESSION = "perqara-session"
    val KOSONG = "session-kosong"

    companion object {
        @SuppressLint("CommitPrefEdits")
        fun instance(context: Context): SessionPref {
            val session = SessionPref()
            session.sharedPreferences =
                context.getSharedPreferences(session.NAME_SESSION, Context.MODE_PRIVATE)
            session.spEditor = session.sharedPreferences!!.edit()
            return session
        }
    }

    fun setTokenLogin(data: String){
        spEditor?.putString("token", data)
        spEditor?.commit()
    }

    fun tokenLogin(): String? {
        return sharedPreferences!!.getString("token", KOSONG)
    }
}