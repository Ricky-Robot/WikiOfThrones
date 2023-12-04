package com.rickyrobot.wikiofthrones.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rickyrobot.wikiofthrones.R
import com.rickyrobot.wikiofthrones.databinding.ActivityMainBinding
import com.rickyrobot.wikiofthrones.model.Character
import com.rickyrobot.wikiofthrones.network.GameOfThronesApi
import com.rickyrobot.wikiofthrones.network.RetrofitService
import com.rickyrobot.wikiofthrones.ui.adapters.CharactersAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.black) // Cambia el color de la barra de notificacion aquí
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val call = RetrofitService.getRetrofit().create(GameOfThronesApi::class.java).getCharacters()

        call.enqueue(object: Callback<ArrayList<Character>>{
            override fun onResponse(
                call: Call<ArrayList<Character>>,
                response: Response<ArrayList<Character>>
            ) {
                binding.pbConexion.visibility = View.INVISIBLE

                val charactersAdapter = CharactersAdapter(response.body()!!){character ->
                    Toast.makeText(this@MainActivity, "${getString(R.string.api_click_personaje)} ${character.firstName}", Toast.LENGTH_SHORT).show()

                    val bundle = bundleOf(
                        "id" to character.id
                    )

                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

                binding.rvMenu.adapter = charactersAdapter
            }

            override fun onFailure(call: Call<ArrayList<Character>>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity,
                    getString(R.string.api_sin_conexion),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        /*-----------------------------------------------------------------------------------------*/

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