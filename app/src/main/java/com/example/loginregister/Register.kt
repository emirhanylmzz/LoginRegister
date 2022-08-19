package com.example.loginregister

import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData

class Register : AppCompatActivity() {
    lateinit var editTextFullname: TextInputEditText
    lateinit var editTextEmail: TextInputEditText
    lateinit var editTextUsername: TextInputEditText
    lateinit var editTextPassword: TextInputEditText
    lateinit var loginText: TextView
    lateinit var buttonSignUp : Button
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        define()
        action()
    }

    fun define(){
        editTextFullname = findViewById(R.id.fullname)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        editTextEmail = findViewById(R.id.email)
        editTextUsername = findViewById(R.id.username)
        editTextPassword = findViewById(R.id.password)
        loginText = findViewById(R.id.loginText)
        progressBar = findViewById(R.id.progress)
    }

    fun action(){
        buttonSignUp.setOnClickListener(){
            var fullname : String; var username : String; var password : String; var email : String

            fullname = editTextFullname.text.toString()
            username = editTextUsername.text.toString()
            password = editTextPassword.text.toString()
            email = editTextEmail.text.toString()

            if(!fullname.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty() && email.isValidEmail()) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(4)
                    field[0] = "fullname"
                    field[1] = "username"
                    field[2] = "password"
                    field[3] = "email"
                    //Creating array for data
                    val data = arrayOfNulls<String>(4)
                    data[0] = fullname
                    data[1] = username
                    data[2] = password
                    data[3] = email
                    val putData = PutData(
                        "http://192.168.1.23:8080/LoginRegister/signup.php",
                        "POST",
                        field,
                        data
                    )
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE
                            val result: String = putData.getResult()
                            if(result.equals("Sign Up Success")){
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                            }
                            //End ProgressBar (Set visibility to GONE)
                            Log.i("PutData", result)
                        }
                    }
                })
            }
            else
            {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }

        }
        loginText.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}