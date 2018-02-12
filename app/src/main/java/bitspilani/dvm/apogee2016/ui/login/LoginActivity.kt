package bitspilani.dvm.apogee2016.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.utils.URL
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_screen.*
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Vaibhav on 11-02-2018.
 */

class LoginActivity : BaseActivity(), LoginMvpView, View.OnClickListener {

    val RC_SIGN_IN = 12

    @Inject
    lateinit var loginPresenter: LoginPresenter<LoginMvpView>

    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        getActivityComponent().inject(this)
        loginPresenter.onAttach(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = Color.parseColor("#FF0077")

        googleSignIn.setSize(SignInButton.SIZE_WIDE)
        googleSignIn.setOnClickListener(this)
        switchButton.setOnClickListener(this)
        outstationParticipantLogin.setOnClickListener(this)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("261999725665-0od42qs76fco9d4d5pf9pe97e4q97jv4.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Get last signed in account
        val account = GoogleSignIn.getLastSignedInAccount(this)

        // If it is null, then user is not signed in. If not, he is already signed in
        if (account != null)
            signedIn(account)

    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.googleSignIn -> signIn()
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
                Toast.makeText(this, "Outstii", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signedIn(account: GoogleSignInAccount) {
        Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show()
        Log.e("profilePic", account.photoUrl?.toString())

        val obj = JSONObject()
        obj.put("id_token", account.idToken)
//        Log.e("id_token", account.idToken)

        AndroidNetworking.post(URL.API_TOKEN)
                .addJSONObjectBody(obj)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("response", response.toString())
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("Error!", "eee")
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
            Snackbar.make(root, "Error! Please try again", Snackbar.LENGTH_LONG).show()
        }

    }
}