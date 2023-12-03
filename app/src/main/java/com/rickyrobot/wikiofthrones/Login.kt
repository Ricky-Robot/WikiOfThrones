package com.rickyrobot.wikiofthrones

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.rickyrobot.wikiofthrones.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email: String = ""
    private var contrasena: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        with(binding){
            binding.botonLogin.setOnClickListener{
                if(!validaCampos()) return@setOnClickListener

                autenticaUsuario(email, contrasena)
            }

            binding.botonRegistrar.setOnClickListener {
                if(!validaCampos()) return@setOnClickListener

                registraUsuario(email, contrasena)
            }

            binding.tvRestablecerPassword.setOnClickListener {
                val resetEmail = EditText(it.context)
                resetEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                val passwordResetDialog = AlertDialog.Builder(it.context)
                    .setTitle(getString(R.string.reset_title))
                    .setMessage(getString(R.string.reset_correo))
                    .setView(resetEmail)
                    .setPositiveButton(getString(R.string.reset_enviar)){_, _ ->
                        val mail = resetEmail.text.toString()
                        if(mail.isNotEmpty()){
                            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                                Toast.makeText(this@Login, getString(R.string.reset_correo_enviado), Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener{
                                Toast.makeText(this@Login, getString(R.string.reset_correo_noenviado), Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this@Login, getString(R.string.reset_email_empty), Toast.LENGTH_SHORT).show()
                        }
                    }.setNegativeButton("Cancelar"){dialog, _ ->
                        dialog.dismiss()
                    }.create().show()
            }
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.editTextEmail.text.toString().trim()
        contrasena = binding.editTextPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.editTextEmail.error = getString(R.string.error_email)
            binding.editTextEmail.requestFocus()
            return false
        }

        if(contrasena.isEmpty() || contrasena.length < 6){
            binding.editTextPassword.error = getString(R.string.error_contrasena)
        }

        return true
    }

    private fun manejarErroresFirebase(exception: Exception, context: Context) {
        val mensajeError: String = when (exception) {
            is FirebaseAuthInvalidUserException -> getString(R.string.firebase_usuario_invalido)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.firebase_credenciales_invalidas)
            is FirebaseNetworkException -> getString(R.string.firebase_error_red)
            is FirebaseAuthUserCollisionException -> getString(R.string.firebase_conflicto_usuarios)
            else -> getString(R.string.firebase_error_desconocido) + ": ${exception.message}"
        }

        // Muestra el mensaje de error en un Toast
        Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show()
    }

    private fun autenticaUsuario(email: String, pass: String){
        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                // Ocultar el ProgressBar después de completar la operación, sea éxito o fallo
                binding.progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.firebase_autenticacion_exitosa), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Inicio de sesión fallido, manejar el error con la función de manejo de errores
                    task.exception?.let { exception ->
                        manejarErroresFirebase(exception, this)
                    }
                }
            }
    }

    private fun registraUsuario(email: String, pass: String){
        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                // Ocultar el ProgressBar después de completar la operación, sea éxito o fallo
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user_fb = firebaseAuth.currentUser

                    user_fb?.sendEmailVerification()?.addOnSuccessListener{
                        Toast.makeText(this, getString(R.string.firebase_correo_verificacion), Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener{
                        Toast.makeText(this, getString(R.string.firebase_correo_noenviado), Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, getString(R.string.firebase_registro_exitoso), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Inicio de sesión fallido, manejar el error con la función de manejo de errores
                    task.exception?.let { exception ->
                        manejarErroresFirebase(exception, this)
                    }
                }
            }
    }
}