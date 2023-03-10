package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import pt.ismai.ac.final2.databinding.ActivityMainMenuBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class MainMenuActivity : AppCompatActivity() {
    // ------------------------------------------ Funções ------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private lateinit var binding: ActivityMainMenuBinding
    lateinit var btnBrowse: Button
    lateinit var btnRandom: Button
    lateinit var btnFavorites: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Atribuições
        btnBrowse = binding.menuBrowse
        btnRandom = binding.menuRandom
        btnFavorites = binding.menuFavorites

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#13012C"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        // Mensagem Login
        val success = intent.getStringExtra("success")
        if (success != null) {
            MotionToast.darkColorToast(
                this,
                "Successful Login!",
                "You've Logged In Successfully.",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

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
        btnFavorites.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, FavoriteDrinksActivity::class.java)
            startActivity(intent)
        }
    }
}