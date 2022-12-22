package pt.ismai.ac.final2
import com.google.gson.Gson

data class Drink(val strDrink: String, val strDrinkThumb: String, val idDrink: String)
data class DrinksResponse(val drinks: List<Drink>)

