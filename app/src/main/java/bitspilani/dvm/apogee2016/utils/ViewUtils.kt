package bitspilani.dvm.apogee2016.utils

import android.content.res.Resources

/**
 * Created by Vaibhav on 07-02-2018.
 */

fun Int.toDp(): Float {
    val densityDpi = Resources.getSystem().displayMetrics.densityDpi
    return this / (densityDpi / 160f)
}

fun Int.toPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return Math.round(this * density)
}

fun Float.toDp(): Float {
    val densityDpi = Resources.getSystem().displayMetrics.densityDpi
    return this / (densityDpi / 160f)
}

fun Float.toPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return Math.round(this * density)
}