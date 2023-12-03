package com.rickyrobot.wikiofthrones

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rickyrobot.wikiofthrones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#454D38") // Cambia el color de la barra de notificacion aquí
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser

        binding.botonCerrarSesion.setOnClickListener {
            Toast.makeText(this, getString(R.string.firebase_sesion_cerrada), Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}