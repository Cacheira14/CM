package pt.ismai.ac.final2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DrinksAdapter(private val drinks: List<Drink>) : RecyclerView.Adapter<DrinksAdapter.ViewHolder>() {

    // Declare the custom click listener interface
    interface OnDrinkClickListener {
        fun onDrinkClick(drink: Drink)
    }

    // Add a click listener field to the adapter
    private lateinit var clickListener: OnDrinkClickListener

    // Add a setter method for the click listener
    fun setOnDrinkClickListener(clickListener: OnDrinkClickListener) {
        this.clickListener = clickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_drinks, parent, false)

        val viewHolder = ViewHolder(view) // Para que ViewHolder seja utilizavel dentro do Listener
        view.setOnClickListener {
            // Guardar nome da bebida e começar nova activity
            val drink = drinks[viewHolder.adapterPosition]
            val drinkName = drink.strDrink

            val intent = Intent(view.context, DrinkDetailsActivity::class.java) //Chamar activity
            intent.putExtra("drink_name", drinkName)
            view.context.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = drinks[position]
        holder.textView.text = drink.strDrink

        // Click Listener
        holder.itemView.setOnClickListener {
            clickListener.onDrinkClick(drink)
        }

        // Glide para carregar imagem
        Glide.with(holder.imageView)
            .load(drink.strDrinkThumb)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return drinks.size
    }
}