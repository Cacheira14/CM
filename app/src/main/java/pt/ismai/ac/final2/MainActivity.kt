package pt.ismai.ac.final2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var binding: ActivityMainBinding
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerDrinks = binding.recyclerNonAlcoholicDrinks
        recyclerDrinks.layoutManager = LinearLayoutManager(this)

        //teste


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
                val adapter = DrinksAdapter(drinkList.sortedBy { it.strDrink })
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
            }
        }).start()


    }
}