package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ismai.ac.final2.databinding.ActivityFavoriteDrinksBinding
import java.io.IOException

class FavoriteDrinksActivity : AppCompatActivity() {
    // ------------------------------------------ Funções ------------------------------------------
    fun itThroughIDs(IDList: List<Int>): List<Drink> {
        //Imports
        val client = OkHttpClient()
        val gson = Gson()

        var drinksDetailsResponse: DrinkDetailsResponse
        var saida: List<Drink> = emptyList()
        var temp: List<DrinkDetails> = emptyList()

        for (ID in IDList) {
            //Construção da request
            val url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=${ID}"
            val request = Request.Builder()
                .url(url)
                .build()
            //Processamento da resposta
            val response = client.newCall(request).execute()
            val responseBody = response.body!!.string() // Necessario pois response.body é consumido na proxima linha
            drinksDetailsResponse = gson.fromJson(responseBody, DrinkDetailsResponse::class.java)
            temp += drinksDetailsResponse.drinks
        }
        saida = convertDrinkDetailsToDrinks(temp)
        return saida
    }

    //Conversor
    fun convertDrinkDetailsToDrinks(drinkDetailsList: List<DrinkDetails>): List<Drink> {
        return drinkDetailsList.map {
            Drink(it.strDrink, it.strDrinkThumb, it.idDrink.toString())
        }
    }

    // ---------------------------------------------------------------------------------------------

    private lateinit var recyclerDrinks: RecyclerView
    private lateinit var adapter: FavoriteDrinksAdapter
    private lateinit var loading2: ImageView

    private lateinit var binding: ActivityFavoriteDrinksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityFavoriteDrinksBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#13012C"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        //Atribuições
        recyclerDrinks = binding.recyclerDrinks
        recyclerDrinks.layoutManager = LinearLayoutManager(this)
        loading2 = binding.loading2
        val favoriteDrinksDbHelper = FavoriteDrinksDbHelper(this)

        //Atribuir imagem a loading
        Glide.with(this) // Glide para carregar imagem com base no URL
            .load("https://media.tenor.com/On7kvXhzml4AAAAj/loading-gif.gif")
            .into(loading2)

        var drinkList: List<Drink> = emptyList()
        Thread(Runnable {
            try {
                val ids = favoriteDrinksDbHelper.getAllIDs()
                drinkList = itThroughIDs(ids) // Itera pelos IDs fornecidos
            } catch (e: IOException) {
                // Por fazer
            }
            runOnUiThread { // Correr na thread que criou a UI
                adapter = FavoriteDrinksAdapter(drinkList.sortedBy { it.strDrink })
                adapter.setOnDrinkClickListener(object : FavoriteDrinksAdapter.OnDrinkClickListener {
                    override fun onDrinkClick(drink: Drink) {
                        // Guardar nome da bebida e começar nova activity
                        val drinkName = drink.strDrink
                        val intent = Intent(this@FavoriteDrinksActivity, DrinkDetailsActivity::class.java) //Preparar chamada de activity
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

    }
}