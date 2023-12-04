package com.rickyrobot.wikiofthrones.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rickyrobot.wikiofthrones.R
import com.rickyrobot.wikiofthrones.databinding.ActivityDetailsBinding
import com.rickyrobot.wikiofthrones.model.Character
import com.rickyrobot.wikiofthrones.model.CharacterDetail
import com.rickyrobot.wikiofthrones.network.GameOfThronesApi
import com.rickyrobot.wikiofthrones.network.RetrofitService
import com.rickyrobot.wikiofthrones.ui.adapters.CharactersAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        val id = bundle?.getString("id", "0")

        val call = RetrofitService.getRetrofit()
            .create(GameOfThronesApi::class.java)
            .getCharacter(id!!.toInt())

        call.enqueue(object: Callback<CharacterDetail>{

            override fun onFailure(call: Call<CharacterDetail>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@DetailsActivity,
                    getString(R.string.api_sin_conexion),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<CharacterDetail>,
                response: Response<CharacterDetail>
            ) {
                binding.pbConexion.visibility = View.INVISIBLE

                binding.tvFullName.text = response.body()?.fullName

                Glide.with(this@DetailsActivity).load(response.body()?.image).into(binding.ivImage)
            }
        })
    }
}