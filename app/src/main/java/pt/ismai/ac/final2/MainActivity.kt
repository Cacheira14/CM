package pt.ismai.ac.final2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ismai.ac.final2.databinding.ActivityMainBinding
import java.io.IOException


class MainActivity : AppCompatActivity() {
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

        val client = OkHttpClient()
        val gson = Gson()

        // Request 1
        val request1 = Request.Builder()
            .url("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink")
            .build()
        // Request 2
        val request2 = Request.Builder()
            .url("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Cocktail")
            .build()

        var drinksResponse1: DrinksResponse? = null // Assegurar que drinksResponse1 é utilizavel fora do try{}
        var drinksResponse2: DrinksResponse? = null
        Thread(Runnable {
            try {
                // Request 1
                val response1 = client.newCall(request1).execute()
                val response1Body =
                    response1.body!!.string() // Necessario pois response.body é consumido na proxima linha
                drinksResponse1 = gson.fromJson(response1Body, DrinksResponse::class.java)
                // request 2
                val response2 = client.newCall(request2).execute()
                val response2Body =
                    response2.body!!.string()
                drinksResponse2 = gson.fromJson(response2Body, DrinksResponse::class.java)
            } catch (e: IOException) {
                // Por fazer
            }
            runOnUiThread { // Correr na thread que criou a UI
                // Bebidas não alcoolicas
                val entrada = (drinksResponse1!!.drinks + drinksResponse2!!.drinks).sortedBy {it.strDrink}
                val adapter = DrinksAdapter(entrada)
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