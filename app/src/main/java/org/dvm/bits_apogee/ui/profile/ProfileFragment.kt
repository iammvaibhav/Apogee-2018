package org.dvm.bits_apogee.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.ui.base.BaseFragment
import org.dvm.bits_apogee.ui.login.LoginActivity
import org.dvm.bits_apogee.utils.URL
import org.dvm.bits_apogee.utils.toPx
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

    lateinit var profileDetails: LinearLayout
    lateinit var loggedOutMessage: FrameLayout
    lateinit var message: TextView
    lateinit var signIn: Button

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
        profileDetails = view.findViewById(R.id.profileDetails)
        loggedOutMessage = view.findViewById(R.id.loggedOutMessage)
        message = view.findViewById(R.id.message)
        signIn = view.findViewById(R.id.signIn)

        message.text = "YOU ARE NOT LOGGED IN\nPlease login to access QR Code"

        if (!profilePresenter.getDataManager().getUserLoggedIn()) {
            loggedOutMessage.visibility = View.VISIBLE
            profileDetails.visibility = View.GONE
        } else {
            loggedOutMessage.visibility = View.GONE
            profileDetails.visibility = View.VISIBLE
            afterLogin()
        }

        signIn.setOnClickListener {
            if (activity != null)
                startActivityForResult(Intent(activity, LoginActivity::class.java), 69)
        }

        logout.setOnClickListener {
            profilePresenter.getDataManager().setUserLoggedIn(false)
            profilePresenter.getDataManager().setCurrentUserName("")
            profilePresenter.getDataManager().setQrCode("")
            profilePresenter.getDataManager().setCurrentUserProfileURL("")
            profilePresenter.getDataManager().setSignedEvents("")
            qrcodeImage.setImageBitmap(null)
            loggedOutMessage.visibility = View.VISIBLE
            profileDetails.visibility = View.GONE
        }

        return view
    }

    fun afterLogin() {
        progressBar.visibility = View.VISIBLE

        name.text = profilePresenter.getDataManager().getCurrentUserName()
        who.text = profilePresenter.getDataManager().getCurrentUserProfileURL()
        //qrcode.text = profilePresenter.getDataManager().getQrCode()
        signedEvents.text = profilePresenter.getDataManager().getSignedEvents()


        if (profilePresenter.getDataManager().getQrCode() != "") {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(profilePresenter.getDataManager().getQrCode(), BarcodeFormat.QR_CODE, 150.toPx(), 150.toPx())
                val width = bitMatrix.width
                val height = bitMatrix.height
                Thread {
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                        }
                    }
                    progressBar.post { qrcodeImage.setImageBitmap(bmp) }
                }.start()

            } catch (e: WriterException) {
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
                            Log.e("responsee", response.getString("token"))
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
                                                profilePresenter.getDataManager().setQrCode(response.getJSONObject("wallet").getString("uid") ?: "")

                                                if (who1 == "bitsian") {
                                                    profilePresenter.getDataManager().setCurrentUserProfileURL("BITS Pilani")
                                                }else {
                                                    profilePresenter.getDataManager().setCurrentUserProfileURL(response.getJSONObject(who1).getString("college_name") ?: "")
                                                }

                                                if (profilePresenter.getDataManager().getQrCode() != "") {
                                                    val writer = QRCodeWriter()
                                                    try {
                                                        val bitMatrix = writer.encode(profilePresenter.getDataManager().getQrCode(), BarcodeFormat.QR_CODE, 150.toPx(), 150.toPx())
                                                        val width = bitMatrix.width
                                                        val height = bitMatrix.height
                                                        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                                                        for (x in 0 until width) {
                                                            for (y in 0 until height) {
                                                                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                                                            }
                                                        }
                                                        qrcodeImage.setImageBitmap(bmp)

                                                    } catch (e: WriterException) {
                                                        e.printStackTrace()
                                                    }
                                                }

                                                val profShows = response.getJSONArray("prof_shows")
                                                val signedEvents1 = StringBuilder()
                                                for (i in 0 until profShows.length()) {
                                                    val profshow = profShows.getJSONObject(i)
                                                    val name = "${profshow.getString("prof_show_name")} | ID : ${profshow.getInt("number")}\nPasses Left: ${profshow.getInt("count")} | Used : ${profshow.getInt("passed_count")}\n\n"
                                                    signedEvents1.append(name)
                                                }

                                                if (signedEvents1.toString() == "")
                                                    signedEvents1.append("None")

                                                profilePresenter.getDataManager().setSignedEvents(signedEvents1.toString())
                                                name.text = response.getJSONObject(who1).getString("name") ?: ""
                                                who.text = profilePresenter.getDataManager().getCurrentUserProfileURL()
                                                //qrcode.text = response.getJSONObject(who1).getString("barcode") ?: ""
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
                                            Log.e("dsf", anError?.errorBody ?: "" )
                                            Log.e("dsf", anError?.errorDetail ?: "" )
                                            Log.e("dsf", anError?.errorCode.toString() ?: "" )
                                        }
                                    })
                        }catch (e: Exception) {
                            e.printStackTrace()
                            onError("Error! Please try again")
                            progressBar.visibility = View.INVISIBLE
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("dsf", anError?.errorBody ?: "" )
                        Log.e("dsf", anError?.errorDetail ?: "" )
                        Log.e("dsf", anError?.errorCode.toString())
                        progressBar.visibility = View.INVISIBLE
                    }
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 69) {
            if (resultCode == RESULT_OK) {
                loggedOutMessage.visibility = View.GONE
                profileDetails.visibility = View.VISIBLE
                afterLogin()
            }else {
                if (activity != null)
                    Toast.makeText(activity, "Please login to view profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profilePresenter.onDetach()
    }
}