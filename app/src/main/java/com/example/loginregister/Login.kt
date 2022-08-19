package com.example.loginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData

class Login : AppCompatActivity() {

    lateinit var editTextUsername: TextInputEditText
    lateinit var editTextPassword: TextInputEditText
    lateinit var signUpText: TextView
    lateinit var buttonLogin : Button
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        define()
        action()
    }

    fun define(){
        editTextUsername = findViewById(R.id.usernameLogin)
        editTextPassword = findViewById(R.id.passwordLogin)
        signUpText = findViewById(R.id.signUpText)
        buttonLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressLogin)
    }

    fun action() {

        buttonLogin.setOnClickListener() {
            var fullname: String;
            var username: String;
            var password: String;
            var email: String

            username = editTextUsername.text.toString()
            password = editTextPassword.text.toString()

            if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(2)
                    field[0] = "username"
                    field[1] = "password"

                    //Creating array for data
                    val data = arrayOfNulls<String>(2)
                    data[0] = username
                    data[1] = password
                    val putData = PutData(
                        "http://192.168.1.23:8080/LoginRegister/login.php",
                        "POST",
                        field,
                        data
                    )
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE
                            val result: String = putData.result
                            if (result.equals("Login Success")) {
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                            }
                            //End ProgressBar (Set visibility to GONE)
                            Log.i("PutData", result)
                        }
                    }
                })
            } else {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }

        }

        signUpText.setOnClickListener(){
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }


}