package com.example.supermoviev1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superheroes.databinding.ItemSupermovieBinding
import com.squareup.picasso.Picasso

class SuperMovieAdapter(private var items:List<supermovie> = listOf(),
                       private val onClickListener: (position:Int) -> Unit
) : RecyclerView.Adapter<supermovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        val binding = ItemSupermovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupermovieViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener { onClickListener(position) }
    }

    fun updateItems(results: List<supermovie>) {
        items = results
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(val binding: ItemSupermovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(supermovie: supermovie) {
        binding.nameTextView.text = supermovie.name
        Picasso.get().load(supermovie.image.url).into(binding.photoImageView)
    }

}
