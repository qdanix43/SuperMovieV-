package com.example.supermoviev1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.supermoviev1.R
import com.example.supermoviev1.adapters.SuperMovieAdapter
import com.example.supermoviev1.data.SuperMovieServiceApi
import com.example.supermoviev1.data.Supermovie
import com.example.supermoviev1.databinding.ActivityMainBinding
import com.example.supermoviev1.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: SuperMovieAdapter
    private var supermovieList: List<Supermovie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SuperMovieAdapter() {
            onItemClickListener(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.progress.visibility = View.GONE
        binding.emptyPlaceholder.visibility = View.VISIBLE

        /*// Configuración del OnClickListener para el botón "Acceder"
        binding.accederButton.setOnClickListener {
            // Llamar a la función para buscar todas las películas
            searchSupermovie("")
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu items from XML
        if (menuInflater == null || menu == null) {
            // Handle error, menuInflater or menu is null
            return false
        }
        menuInflater.inflate(R.menu.main_menu, menu)

        // Initialize search view
        initSearchView(menu.findItem(R.id.menu_search))

        return true
    }


    private fun initSearchView(searchItem: MenuItem?) {
        if (searchItem != null) {
            var searchView =
                (searchItem.actionView as SearchView).also {

                    it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            searchSupermovie(query!!)
                            it.clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(query: String?): Boolean {
                            return false
                        }
                    })
                }
        }
    }

    private fun onItemClickListener(position: Int) {
        val superMovieItem: Supermovie = supermovieList[position]

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, superMovieItem.imdbID)
        intent.putExtra(DetailActivity.EXTRA_NAME, superMovieItem.title)
        intent.putExtra(DetailActivity.EXTRA_IMAGE, superMovieItem.poster)
        startActivity(intent)
    }

    private fun searchSupermovie(query: String) {
        binding.progress.visibility = View.VISIBLE

        val service: SuperMovieServiceApi = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchByName(query)

            runOnUiThread {
                binding.progress.visibility = View.GONE

                if (response.isSuccessful) {
                    Log.i("HTTP", "respuesta correcta :)")
                    val superMovieResponse = response.body()
                    if (superMovieResponse != null && superMovieResponse.response == "True") {
                        supermovieList = superMovieResponse.search
                        adapter.updateItems(supermovieList)

                        if (supermovieList.isNotEmpty()) {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyPlaceholder.visibility = View.GONE
                        } else {
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyPlaceholder.visibility = View.VISIBLE
                        }
                    } else {
                        Log.i("HTTP", "respuesta con error :(")
                        Toast.makeText(
                            this@MainActivity,
                            "No se encontraron resultados",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                    Toast.makeText(
                        this@MainActivity,
                        "Hubo un error inesperado, vuelva a intentarlo más tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
