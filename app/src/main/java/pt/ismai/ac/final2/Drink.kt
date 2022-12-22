package pt.ismai.ac.final2
import com.google.gson.Gson

data class Drink(val strDrink: String, val strDrinkThumb: String, val idDrink: String)
data class DrinksResponse(val drinks: List<Drink>)

data class DrinkCategories(val strCategory: String)
data class DrinkCategoriesResponse(val drinks: List<DrinkCategories>)
