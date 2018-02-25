package org.dvm.bits_apogee.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_screen.*
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.dataManager
import org.dvm.bits_apogee.ui.base.BaseActivity
import org.dvm.bits_apogee.utils.URL
import org.json.JSONObject

/**
 * Created by Vaibhav on 11-02-2018.
 */

class LoginActivity : BaseActivity(), LoginMvpView, View.OnClickListener {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    val RC_SIGN_IN = 12

    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = Color.parseColor("#FF0077")

        googleSignIn.setSize(SignInButton.SIZE_WIDE)
        googleSignIn.setOnClickListener(this)
        switchButton.setOnClickListener(this)
        outstationParticipantLogin.setOnClickListener(this)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("975053221879-9dvv66a20imkc2f460pcepa41drds9nt.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        /*// Get last signed in account
        val account = GoogleSignIn.getLastSignedInAccount(this)

        // If it is null, then user is not signed in. If not, he is already signed in
        if (account != null)
            signedIn(account)*/

    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.googleSignIn -> {
                progress.visibility = View.VISIBLE
                if (GoogleSignIn.getLastSignedInAccount(this) != null)
                    googleSignInClient.signOut().addOnCompleteListener {
                        signIn()
                    }
                else signIn()
            }
            R.id.switchButton -> {
                if (switchButton.text == "outstation participant") {
                    switchButton.text = "bits student"
                    whoIsSigningLabel.text = "outstation participant"
                    googleSignIn.visibility = View.GONE
                    outstationParticipant.visibility = View.VISIBLE
                }else {
                    whoIsSigningLabel.text = "bits student"
                    switchButton.text = "outstation participant"
                    googleSignIn.visibility = View.VISIBLE
                    outstationParticipant.visibility = View.GONE
                }
            }
            R.id.outstationParticipantLogin -> {
                progress.visibility = View.VISIBLE

                val credentials = JSONObject()
                credentials.put("username", input_email.text)
                credentials.put("password", input_password.text)
                AndroidNetworking.post(URL.API_TOKEN)
                        .addJSONObjectBody(credentials)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                if (response.has("token")) {
                                    dataManager.getDataManager().setUserLoggedIn(true)
                                    dataManager.getDataManager().setCurrentUserUsername(credentials.getString("username"))
                                    dataManager.getDataManager().setCurrentUserPassword(credentials.getString("password"))
                                    dataManager.getDataManager().setIsBitsian(false)

                                    val intent = Intent()
                                    intent.putExtra("username", credentials.getString("username"))
                                    intent.putExtra("password", credentials.getString("password"))
                                    progress.visibility = View.INVISIBLE
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                    Log.e("finished", "")
                                }else {
                                    progress.visibility = View.INVISIBLE
                                    Log.e("fdsf", "error")
                                    onError("Invalid Login!")
                                }
                            }

                            override fun onError(anError: ANError?) {
                                progress.visibility = View.INVISIBLE
                                onError("Invalid Login!")
                                Log.e("fdsf", "error2")
                            }
                        })
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun signedIn(account: GoogleSignInAccount) {
        Log.e("token", account.idToken)

        val obj = JSONObject()
        obj.put("id_token", account.idToken)

        AndroidNetworking.post(URL.GET_USERNAME)
                .addJSONObjectBody(obj)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        progress.visibility = View.INVISIBLE
                        if (response.has("username") && response.has("password")) {
                            dataManager.getDataManager().setUserLoggedIn(true)
                            dataManager.getDataManager().setCurrentUserUsername(response.getString("username"))
                            dataManager.getDataManager().setCurrentUserPassword(response.getString("password"))
                            dataManager.getDataManager().setIsBitsian(true)
                            val intent = Intent()
                            Log.e("hio", "i ma here")
                            intent.putExtra("username", response.getString("username"))
                            intent.putExtra("password", response.getString("password"))
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                            return
                        }else {
                            onError("Invalid Login. Make sure you are using Bitsmail")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        progress.visibility = View.INVISIBLE
                        onError("Invalid Login. Make sure you are using Bitsmail")
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            signedIn(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Error", "signInResult:failed code=" + e.statusCode)
            Log.e("Error", "sdjsjdkl" + GoogleSignInStatusCodes.getStatusCodeString(e.statusCode))
            Snackbar.make(root, "Error! Please try again", Snackbar.LENGTH_LONG).show()
        }

    }
}