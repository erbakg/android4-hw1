package com.example.android4_1.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android4_1.App
import com.example.android4_1.R
import com.example.android4_1.databinding.FragmentAuthBinding
import com.example.android4_1.utils.AUTH_TYPES
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [AuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private var authType = AUTH_TYPES.SIGN_IN
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initListeners()
        setupGoogleSignIn()
    }

    private fun setupGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initListeners() {
        binding.btnRegistrationUser.setOnClickListener {
            if (authType == AUTH_TYPES.SIGN_IN) {
                emailPasswordSignIn()
            } else {
                emailPasswordSignUp()
            }

        }
        binding.btnChooseSignIn.setOnClickListener {
            authType = AUTH_TYPES.SIGN_IN
            binding.btnChooseSignIn.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.btnChooseSignIn.setTextColor(resources.getColor(R.color.white))
            binding.btnChooseSignUp.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnChooseSignUp.setTextColor(resources.getColor(R.color.black))
            binding.btnRegistrationUser.setText(resources.getString(R.string.auth_sign_in_btn))
        }
        binding.btnChooseSignUp.setOnClickListener {
            authType = AUTH_TYPES.SIGN_UP
            binding.btnChooseSignUp.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.btnChooseSignUp.setTextColor(resources.getColor(R.color.white))
            binding.btnChooseSignIn.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnChooseSignIn.setTextColor(resources.getColor(R.color.black))
            binding.btnRegistrationUser.setText(resources.getString(R.string.auth_sign_up_btn))

        }
        binding.btnGoogleSignIn.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, /*accessToken=*/ null)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val prefs = (requireContext().applicationContext as App).mySharedPreferense
                    val user = mAuth.currentUser
                    prefs?.setUserAuth(
                        true
                    )
                    prefs?.saveLogin(user?.email.toString())
                    prefs?.saveName(user?.displayName.toString())
                    prefs?.saveAvatar(user?.photoUrl.toString())

                    val intent = requireActivity().intent
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun emailPasswordSignIn() {
        auth.signInWithEmailAndPassword(
            binding.etSignUpLogin.text.toString(),
            binding.etSignUpPassword.text.toString()
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("haha", "signInWithEmail:success")
                    val user = auth.currentUser
                    (requireContext().applicationContext as App).mySharedPreferense?.setUserAuth(
                        true
                    )
                    (requireContext().applicationContext as App).mySharedPreferense?.saveLogin(user?.email.toString())
                    val intent = requireActivity().intent
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("haha", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun emailPasswordSignUp() {
        auth.createUserWithEmailAndPassword(
            binding.etSignUpLogin.text.toString(),
            binding.etSignUpPassword.text.toString()
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    (requireContext().applicationContext as App).mySharedPreferense?.setUserAuth(
                        true
                    )
                    (requireContext().applicationContext as App).mySharedPreferense?.saveLogin(user?.email.toString())
                    val intent = requireActivity().intent
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

}