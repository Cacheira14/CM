package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import pt.ismai.ac.final2.databinding.ActivityMainMenuBinding


class MainMenuActivity : AppCompatActivity() {
    // ------------------------------------------ Funções ------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private lateinit var binding: ActivityMainMenuBinding
    lateinit var btnBrowse: Button
    lateinit var btnRandom: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Atribuições
        btnBrowse = binding.menuBrowse
        btnRandom = binding.menuRandom

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#000000"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        // Chamada de outras activities
        btnBrowse.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, MainActivity::class.java)
            startActivity(intent)
        }
        btnRandom.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, DrinkDetailsActivity::class.java) //Preparar chamada de activity
            intent.putExtra("drink_name", "random") // Enviar String extra (Se string for "random", pesquisa bebida aleatoriamente)
            startActivity(intent) //Start
        }
    }
}