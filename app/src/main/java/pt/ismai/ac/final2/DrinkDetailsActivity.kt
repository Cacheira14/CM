package pt.ismai.ac.final2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ismai.ac.final2.databinding.ActivityDrinkDetailsBinding
import java.io.IOException

class DrinkDetailsActivity : AppCompatActivity() {
    lateinit var drink_name_text_view: TextView
    lateinit var drink_category_text_view: TextView
    lateinit var drink_glass_text_view: TextView
    lateinit var drink_instructions_text_view: TextView
    lateinit var drink_ingredient1_text_view: TextView
    lateinit var drink_thumb_image_view: ImageView

    private lateinit var binding: ActivityDrinkDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityDrinkDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Inicialização de vars
        drink_name_text_view = binding.drinkSpecificNameTextView
        drink_category_text_view = binding.drinkSpecificCategoryTextView
        drink_glass_text_view = binding.drinkSpecificGlassTextView
        drink_instructions_text_view = binding.drinkSpecificInstructionsTextView
        drink_ingredient1_text_view = binding.drinkSpecificIngredient1TextView
        drink_thumb_image_view = binding.drinkSpecificThumbImageView

        // Ir buscar valor importado da main activity
        val drinkName = intent.getStringExtra("drink_name")
        drink_name_text_view.text = drinkName

        val client = OkHttpClient()
        val gson = Gson()

        val request = Request.Builder()
            .url("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=$drinkName")
            .build()

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
                drink_name_text_view.text = drink!!.strDrink
                drink_category_text_view.text = drink!!.strCategory
                drink_glass_text_view.text = drink!!.strGlass
                drink_instructions_text_view.text = drink!!.strInstructions
                drink_ingredient1_text_view.text = drink!!.strIngredient1
                Glide.with(this) // Glide para carregar imagem com base no URL
                    .load(drink!!.strDrinkThumb)
                    .into(drink_thumb_image_view)
            }
        }).start()
    }
}