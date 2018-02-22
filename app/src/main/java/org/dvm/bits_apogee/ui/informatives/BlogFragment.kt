package org.dvm.bits_apogee.ui.informatives

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.ui.base.BaseFragment

/**
 * Created by vaibhav on 20/2/18.
 */

class BlogFragment : BaseFragment() {

    lateinit var webview: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)
        webview = view.findViewById(R.id.webView)
        webview.loadUrl("https://epcbits.wordpress.com")

        return view
    }
}