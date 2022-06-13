package com.capstone.temanhewan.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.temanhewan.R
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Utils {
    fun AppCompatActivity.simpleToolbar(
        toolbarTitle: String,
        toolbar: Toolbar?,
        navigationIcon: Boolean
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(navigationIcon)
        }
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.getStringFromName(name: String): String {
        return getString(resources.getIdentifier(name, "string", packageName))
    }

    fun Button.setStateColor(context: Context, backgroundColor: Int, enable: Boolean) {
        setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
        isEnabled = enable
    }

    fun ImageView.loadImageUrl(url: String) {
        Glide.with(this.context.applicationContext)
            .load(url)
            .circleCrop()
            .error(R.drawable.ic_baseline_broken_image_24)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24))
            .into(this)
    }

    fun ImageView.loadImageURI(uri: Uri) {
        Glide.with(this.context.applicationContext)
            .asBitmap()
            .load(uri)
            .circleCrop()
            .into(this)
    }
}
object Time {

    fun timeStamp(): String {

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}
object SolveMath {

    fun solveMath(equation: String) : Int{

        val newEquation = equation.replace(" ", "")
        Log.d("Math", newEquation)

        return when {
            newEquation.contains("+") -> {
                val split = newEquation.split("+")
                val result = split[0].toInt() + split[1].toInt()
                result
            }
            newEquation.contains("-") -> {
                val split = newEquation.split("-")
                val result = split[0].toInt() - split[1].toInt()
                result
            }
            newEquation.contains("*") -> {
                val split = newEquation.split("*")
                val result = split[0].toInt() * split[1].toInt()
                result
            }
            newEquation.contains("/") -> {
                val split = newEquation.split("/")
                val result = split[0].toInt() / split[1].toInt()
                result
            }
            else -> {
                0
            }
        }
    }
}