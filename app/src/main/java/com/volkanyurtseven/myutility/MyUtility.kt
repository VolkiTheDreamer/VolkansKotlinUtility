package com.volkanyurtseven.myutility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt


object MyUtility {

    fun getTodayAsString():String {
        val time = Calendar.getInstance().time
        var formatterSDF = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var current = formatterSDF.format(time)
        return current
    }

    fun getLocalCountryCode(): String {
        val localInfo = Locale.getDefault()
        val countryCode = localInfo.country
        return countryCode
    }
    fun isOnline(): Boolean {
        //main threadde çalışmıyor ama çok hızlıymış
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
    fun isInternetAvailable(): Boolean {
        //false döndü
        return try {
            val ipAddr = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }

/*
public boolean InternetKontrol() {
 ConnectivityManager manager = (ConnectivityManager) 
getSystemService(Context.CONNECTIVITY_SERVICE);
 if (manager.getActiveNetworkInfo() != null
 && manager.getActiveNetworkInfo().isAvailable()
 && manager.getActiveNetworkInfo().isConnected()) {
 return true;
 } else {
 return false;
 }
}

*/

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

}

//Extension functions, from https://medium.com/@akshay.kalola28/10-useful-kotlin-extension-functions-for-android-developer-7614ef800c3d
fun Activity.showtoast_(message: String, duration: Int = Toast.LENGTH_LONG) {
    //burda variable olarak da yaratıp konumunu falan atayabiliriz
    Toast.makeText(this, message, duration).show()
}

//showneutral: uzun mesajlar kalıcı olsun ve ortada beyaz olsun istiyorsak
//showsnack: kısa kalıcı altta siyah
//asyncten bağımsız, hatta finish sonrası bile görüncekse(ör:internet yok uyarısından sonra), uzun mesaj ok ama kalıcı değil
fun Activity.showNeutralAlertDialog_(message:String, buttonText:String=getString(R.string.OK), targetActivity: Class<Any>?=null) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setNeutralButton(buttonText){x,y ->
        if (targetActivity != null)
            startActivity(Intent(this,targetActivity))
    }
    builder.show()
}
fun View.showSnackbar_(message: String, duration: Int = Snackbar.LENGTH_LONG, isAction: Boolean=true, actionWord: String=resources.getString(R.string.OK)) {
    if (isAction)
        Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).setAction(actionWord){}.show()
    else
        Snackbar.make(this, message, duration).show()
} //rootView.snackbar("This is snackbar message")

@SuppressLint("RestrictedApi")
fun Activity.hideKeyboard_() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Any?.printToLog_(tag: String = "mytag") {
    Log.d(tag, toString())
}

val String.isDigitOnly_: Boolean
    get() = matches(Regex("^\\d*\$"))

val String.isAlphabeticOnly_: Boolean
    get() = matches(Regex("^[a-zA-Z]*\$"))

val String.isAlphanumericOnly_: Boolean
    get() = matches(Regex("^[a-zA-Z\\d]*\$"))
//val isValidNumber = "1234".isDigitOnly // Return true

fun String.toDate_(format: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.parse(this)
}

fun Date.toStringFormat_(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}

val EditText.value_
    get() = text?.toString() ?: ""

fun String.copyToClipboard_(context: Context) {
    val clipboardManager = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("clipboard", this)
    clipboardManager?.setPrimaryClip(clip)
}

fun Float.roundTo_(n : Int) : Float {
    //return "%.${n}f".format(this).toFloat()
    val power = 10.0.pow(n.toDouble())
    return ((this * power).roundToInt() /power).toFloat()
}

fun Double.roundTo_(n : Int) : Double {
    //return "%.${n}f".format(this).toDouble()
    val power = 10.0.pow(n.toDouble())
    return ((this * power).roundToInt() /power)
}