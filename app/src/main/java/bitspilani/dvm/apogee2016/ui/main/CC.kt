package bitspilani.dvm.apogee2016.ui.main

import android.graphics.Color

/**
 * Created by Vaibhav on 11-02-2018.
 */
class ScreenColor(colorA: String, colorB: String, colorC: String) {
    val colorA = Color.parseColor(colorA)
    val colorB = Color.parseColor(colorB)
    val colorC = Color.parseColor(colorC)
}

object CC{
    val Colors = mutableListOf(ScreenColor("#EF8D0C", "#EF0C0C", "#870F0F"),
            ScreenColor("#0C41EF", "#0CA9EF", "#006491"),
            ScreenColor("#0EE58F", "#10FF9F", "#01A77F"),
            ScreenColor("#8800FF", "#FF0077", "#C7015D"),
            ScreenColor("#0EE58F", "#10FF9F", "#00B288"))

    fun getScreenColorFor(position: Int) = Colors[position % Colors.size]
}