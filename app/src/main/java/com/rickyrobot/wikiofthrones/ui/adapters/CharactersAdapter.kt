package com.rickyrobot.wikiofthrones.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rickyrobot.wikiofthrones.databinding.CharacterElementBinding
import com.rickyrobot.wikiofthrones.model.Character

class CharactersAdapter(private var characters: ArrayList<Character>, private var onCharacterClicked: (Character) -> Unit): RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {
    class ViewHolder(private var binding: CharacterElementBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(character: Character){
            binding.tvName.text = character.firstName
            binding.tvLastName.text = character.lastName

            Glide.with(itemView.context).load(character.imageUrl).into(binding.ivThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterElementBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])

        holder.itemView.setOnClickListener{
            onCharacterClicked(characters[position])
        }
    }
}