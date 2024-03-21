package com.example.supermoviev1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supermoviev1.data.Supermovie
import com.example.supermoviev1.databinding.ItemSupermovieBinding
import com.squareup.picasso.Picasso

class SuperMovieAdapter(
    private var items: List<Supermovie> = listOf(),
    private val onClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<SupermovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupermovieViewHolder {
        val binding = ItemSupermovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupermovieViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SupermovieViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    fun updateItems(results: List<Supermovie>) {
        items = results
        notifyDataSetChanged()
    }
}

class SupermovieViewHolder(private val binding: ItemSupermovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(supermovie: Supermovie, onClickListener: (position: Int) -> Unit) {
        binding.nameTextView.text = supermovie.title
        Picasso.get().load(supermovie.poster).into(binding.photoImageView)
        binding.root.setOnClickListener { onClickListener(adapterPosition) }
    }
}
