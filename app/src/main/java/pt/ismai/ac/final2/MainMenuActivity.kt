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
    private fun executeAnotherActivity(AnotherActivity: Class<*>) {
        val x = Intent(this, AnotherActivity)
        startActivity(x)
    }
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
            executeAnotherActivity(MainActivity::class.java)
        }
        btnRandom.setOnClickListener {
            executeAnotherActivity(DrinkDetailsActivity::class.java) // POR IMPLEMENTAR
        }
    }
}