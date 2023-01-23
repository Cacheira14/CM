package pt.ismai.ac.final2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import pt.ismai.ac.final2.databinding.ActivityLoginBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {
    private lateinit var btn_login: Button
    private lateinit var btn_signup: Button
    private lateinit var txt_email: EditText
    private lateinit var txt_pass: EditText

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Atribuições
        btn_login = binding.loginLogin
        btn_signup = binding.loginSignup
        txt_email = binding.loginEmail
        txt_pass = binding.loginPassword

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#13012C"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        // Mensagem registo
        val success = intent.getStringExtra("success")
        if (success != null) {
            MotionToast.darkColorToast(
                this,
                "Successful Sign Up!",
                "You've Signed Up Successfully.",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        // Login
        btn_login.setOnClickListener {
            //Verificações
            if (txt_email.text.isEmpty() || txt_email.text.isBlank()) {
                MotionToast.darkColorToast(this,
                    "Campo de Email vazio",
                    "Insira um email válido",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            } else if (txt_pass.text.isEmpty() || txt_pass.text.isBlank()) {
                MotionToast.darkColorToast(this,
                    "Campo de Password vazio",
                    "Insira uma password válida",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }
            else{
                val fireAuth = FirebaseAuth.getInstance()
                fireAuth.signInWithEmailAndPassword(txt_email.text.toString(), txt_pass.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = fireAuth.currentUser
                            val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                            intent.putExtra("success", "sucess") // Enviar String extra
                            startActivity(intent)
                        } else {
                            MotionToast.darkColorToast(
                                this,
                                "Login failed!",
                                "Wrong Credentials / Service down.",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                        }
                    }
            }
        }
        btn_signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}