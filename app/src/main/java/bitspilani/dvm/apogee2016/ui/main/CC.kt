package bitspilani.dvm.apogee2016.ui.main

/**
 * Created by Vaibhav on 11-02-2018.
 */

object CC{
    val Colors = mutableListOf(MainActivity.ScreenColor("#EF8D0C", "#EF0C0C", "#870F0F"),
            MainActivity.ScreenColor("#0C41EF", "#0CA9EF", "#006491"),
            MainActivity.ScreenColor("#0EE58F", "#10FF9F", "#01A77F"),
            MainActivity.ScreenColor("#8800FF", "#FF0077", "#C7015D"),
            MainActivity.ScreenColor("#0EE58F", "#10FF9F", "#00B288"))

    fun getScreenColorFor(position: Int) = Colors[position % Colors.size]
}