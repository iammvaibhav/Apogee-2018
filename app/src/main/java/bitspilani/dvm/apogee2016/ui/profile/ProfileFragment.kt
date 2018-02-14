package bitspilani.dvm.apogee2016.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseFragment
import bitspilani.dvm.apogee2016.ui.login.LoginActivity
import bitspilani.dvm.apogee2016.utils.URL
import bitspilani.dvm.apogee2016.utils.toPx
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import javax.inject.Inject


/**
 * Created by Vaibhav on 12-02-2018.
 */

class ProfileFragment : BaseFragment(), ProfileMvpView {

    @Inject
    lateinit var profilePresenter: ProfilePresenter<ProfileMvpView>

    lateinit var progressBar: ProgressBar
    lateinit var name: TextView
    lateinit var who: TextView
    lateinit var qrcode: TextView
    lateinit var logout: Button
    lateinit var qrcodeImage: ImageView
    lateinit var scrollView: ScrollView
    lateinit var signedEvents: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        profilePresenter.onAttach(this)

        progressBar = view.findViewById(R.id.progress1)
        name = view.findViewById(R.id.name)
        who = view.findViewById(R.id.who)
        qrcode = view.findViewById(R.id.qrcode)
        logout = view.findViewById(R.id.logout)
        qrcodeImage = view.findViewById(R.id.qrcodeImage)
        scrollView = view.findViewById(R.id.scrollview)
        signedEvents = view.findViewById(R.id.signedEvents)

        progressBar.visibility = View.VISIBLE

        name.text = profilePresenter.getDataManager().getCurrentUserName()
        who.text = if (profilePresenter.getDataManager().getIsBitsian()) "bitsian" else "outstee"
        qrcode.text = profilePresenter.getDataManager().getQrCode()
        signedEvents.text = profilePresenter.getDataManager().getSignedEvents()

        logout.setOnClickListener {
            profilePresenter.getDataManager().setUserLoggedIn(false)
            profilePresenter.getDataManager().setCurrentUserName("")
            profilePresenter.getDataManager().setQrCode("")
            profilePresenter.getDataManager().setSignedEvents("")
            try {
                activity.startActivityForResult(Intent(activity, LoginActivity::class.java), 69)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        if (profilePresenter.getDataManager().getQrCode() != "") {
            val multiFormatWriter = MultiFormatWriter()
            try{
                val bitMatrix= multiFormatWriter.encode("ae raja raja", BarcodeFormat.QR_CODE, 150.toPx(), 150.toPx())
                val barcodeEncoder = BarcodeEncoder()
                val bitmap =barcodeEncoder.createBitmap(bitMatrix)
                qrcodeImage.setImageBitmap(bitmap)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val credentials = JSONObject()
        credentials.put("username", profilePresenter.getDataManager().getCurrentUserUsername())
        credentials.put("password", profilePresenter.getDataManager().getCurrentUserPassword())
        AndroidNetworking.post(URL.API_TOKEN)
                .addJSONObjectBody(credentials)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            Log.e("response", response.getString("token"))
                            val walletToken = JSONObject()
                            walletToken.put("WALLET_TOKEN", "4b6e39873f40492aadee397b03316b62")
                            AndroidNetworking.post(URL.GET_PROFILE)
                                    .addHeaders("Authorization", "JWT ${response.getString("token")}")
                                    .addJSONObjectBody(walletToken)
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject) {
                                            try {
                                                Log.e("res", response.toString(4))
                                                progressBar.visibility = View.INVISIBLE
                                                val isBitsian = response.getJSONObject("wallet").getBoolean("is_bitsian")
                                                var who1 = if (isBitsian) "bitsian" else "participant"
                                                profilePresenter.getDataManager().setCurrentUserName(response.getJSONObject(who1).getString("name") ?: "")
                                                profilePresenter.getDataManager().setQrCode(response.getJSONObject(who1).getString("barcode") ?: "")
                                                val profShows = response.getJSONArray("prof_shows")
                                                val signedEvents1 = StringBuilder()
                                                for (i in 0 until profShows.length())
                                                    signedEvents1.append("${profShows.getString(i)}\n")
                                                profilePresenter.getDataManager().setSignedEvents(signedEvents1.toString())

                                                name.text = response.getJSONObject(who1).getString("name") ?: ""
                                                qrcode.text = response.getJSONObject(who1).getString("barcode") ?: ""
                                                signedEvents.text = signedEvents1.toString()
                                            }catch (e: Exception) {
                                                e.printStackTrace()
                                                onError("Error! Please try again")
                                                progressBar.visibility = View.INVISIBLE
                                            }
                                        }

                                        override fun onError(anError: ANError?) {
                                            onError("Error! Please try again")
                                            progressBar.visibility = View.INVISIBLE
                                        }
                                    })
                        }catch (e: Exception) {
                            e.printStackTrace()
                            onError("Error! Please try again")
                            progressBar.visibility = View.INVISIBLE
                        }
                    }

                    override fun onError(anError: ANError?) {
                        onError("Error! Please try again")
                        progressBar.visibility = View.INVISIBLE
                    }
                })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 69) {
            if (resultCode == RESULT_OK) {
                Log.e("dff", "fragment result oko")
            }else {
                Log.e("lolwut", "ae raja raja raja")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profilePresenter.onDetach()
    }
}