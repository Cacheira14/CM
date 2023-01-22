package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ismai.ac.final2.databinding.ActivityDrinkDetailsBinding
import java.io.IOException

class DrinkDetailsActivity : AppCompatActivity() {
    lateinit var drink_category_text_view: TextView
    lateinit var drink_glass_text_view: TextView
    lateinit var drink_instructions_text_view: TextView
    lateinit var drink_ingredient1_text_view: TextView
    lateinit var drink_thumb_image_view: ImageView
    private lateinit var loading1: ImageView
    private lateinit var mainLayout1: LinearLayout
    private var drinkID: Int = 0

    private lateinit var binding: ActivityDrinkDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityDrinkDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Inicialização de vars
        drink_category_text_view = binding.drinkSpecificCategoryTextView
        drink_glass_text_view = binding.drinkSpecificGlassTextView
        drink_instructions_text_view = binding.drinkSpecificInstructionsTextView
        drink_ingredient1_text_view = binding.drinkSpecificIngredient1TextView
        drink_thumb_image_view = binding.drinkSpecificThumbImageView
        loading1 = binding.loading1
        mainLayout1 = binding.mainLayout1

        //Atribuir imagem a loading
        Glide.with(this) // Glide para carregar imagem com base no URL
            .load("https://media.tenor.com/On7kvXhzml4AAAAj/loading-gif.gif")
            .into(loading1)

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#13012C"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        val client = OkHttpClient()
        val gson = Gson()

        // Ir buscar valor importado da main activity
        val drinkName = intent.getStringExtra("drink_name")
        val request: Request
        if (drinkName == "random"){
            request = Request.Builder()
                .url("https://www.thecocktaildb.com/api/json/v1/1/random.php\n")
                .build()
        }else {
            request = Request.Builder()
                .url("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=$drinkName")
                .build()
        }
        var drink: DrinkDetails? = null // Assegurar que drink é utilizavel fora do try{}
        Thread(Runnable {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body!!.string()
                val drinkResponse = gson.fromJson(responseBody, DrinkDetailsResponse::class.java)
                drink = drinkResponse.drinks[0]
            } catch (e: IOException) {
                // Por fazer
            }
            runOnUiThread {
                drinkID = drink!!.idDrink
                supportActionBar?.setTitle(drink!!.strDrink) // Definir bebida como titulo da pagina
                drink_category_text_view.text = drink!!.strCategory
                drink_glass_text_view.text = drink!!.strGlass
                drink_instructions_text_view.text = drink!!.strInstructions
                // Lista de Ingredientes
                var ingredientsList: String = ""
                for (i in 1..15) {
                    val fieldName = "strIngredient$i"
                    val field = drink!!::class.java.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val ingredient = field.get(drink) as? String

                    if(i == 1) {
                        ingredientsList += ingredient
                    } else if(ingredient == "" || ingredient == null){
                        break
                    } else {
                        ingredientsList += (", $ingredient")
                    }
                }
                drink_ingredient1_text_view.text = ingredientsList
                Glide.with(this) // Glide para carregar imagem com base no URL
                    .load(drink!!.strDrinkThumb)
                    .into(drink_thumb_image_view)

                // Desativar Loading e ativar Layout Principal
                loading1.visibility = View.GONE
                mainLayout1.visibility = View.VISIBLE
            }
        }).start()
    }
    // Botão favoritos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_button -> {
                val favoriteDrinksDbHelper = FavoriteDrinksDbHelper(this)
                favoriteDrinksDbHelper.addDrink(drinkID)
                //favoriteDrinksDbHelper.removeDrink(1)
                //val ids = favoriteDrinksDbHelper.getAllIDs()
                Log.d("teste", drinkID.toString())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}