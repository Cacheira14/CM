package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ismai.ac.final2.databinding.ActivityMainBinding
import java.io.IOException


class MainActivity : AppCompatActivity() {

    // ------------------------------------------ Funções ------------------------------------------
    fun itThroughCats(categories: DrinkCategoriesResponse): List<Drink> {
        //Imports
        val client = OkHttpClient()
        val gson = Gson()

        var drinksResponse: DrinksResponse
        var saida: List<Drink> = emptyList()

        for (category in categories.drinks) {
            //Construção da request
            val url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=${category.strCategory}"
            val request = Request.Builder()
                .url(url)
                .build()
            //Processamento da resposta
            val response = client.newCall(request).execute()
            val responseBody = response.body!!.string() // Necessario pois response.body é consumido na proxima linha
            drinksResponse = gson.fromJson(responseBody, DrinksResponse::class.java)
            saida += drinksResponse.drinks
        }
        return saida
    }
    // ---------------------------------------------------------------------------------------------

    lateinit var recyclerDrinks: RecyclerView
    private lateinit var adapter: DrinksAdapter
    private lateinit var loading2: ImageView

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#000000"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        //Atribuições
        recyclerDrinks = binding.recyclerNonAlcoholicDrinks
        recyclerDrinks.layoutManager = LinearLayoutManager(this)
        loading2 = binding.loading2

        //Atribuir imagem a loading
        Glide.with(this) // Glide para carregar imagem com base no URL
            .load("https://media.tenor.com/On7kvXhzml4AAAAj/loading-gif.gif")
            .into(loading2)


        val client = OkHttpClient()
        val gson = Gson()

        // Request 1
        val request = Request.Builder()
            .url("https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list")
            .build()

        var drinkList: List<Drink> = emptyList()

        Thread(Runnable {
            try {
                // Request 1 (Lista de categorias)
                val response = client.newCall(request).execute()
                val responseBody =
                    response.body!!.string() // Necessario pois response.body é consumido na proxima linha
                val drinksCategoriesResponse = gson.fromJson(responseBody, DrinkCategoriesResponse::class.java)

                drinkList = itThroughCats(drinksCategoriesResponse) // Itera pelas categorias e obtem todas as bebidas
            } catch (e: IOException) {
                // Por fazer
            }
            runOnUiThread { // Correr na thread que criou a UI
                adapter = DrinksAdapter(drinkList.sortedBy { it.strDrink })
                adapter.setOnDrinkClickListener(object : DrinksAdapter.OnDrinkClickListener {
                    override fun onDrinkClick(drink: Drink) {
                        // Guardar nome da bebida e começar nova activity
                        val drinkName = drink.strDrink
                        val intent = Intent(this@MainActivity, DrinkDetailsActivity::class.java) //Preparar chamada de activity
                        intent.putExtra("drink_name", drinkName) // Enviar String extra
                        startActivity(intent) //Start
                    }
                })
                recyclerDrinks.adapter = adapter
                adapter.notifyDataSetChanged()
                // Desativar loading e ativar Recycler
                loading2.visibility = View.GONE
                recyclerDrinks.visibility = View.VISIBLE
            }
        }).start()

        // ----------------------------------- Componente de procura -----------------------------------
        val searchEvery = findViewById<SearchView>(R.id.searchEvery)
        searchEvery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                // Chamar filter quando é efetuada uma query
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Chamar filter quando é efetuada uma query (Onchange)
                adapter.filter(newText)
                return true
            }
        })
    }
}