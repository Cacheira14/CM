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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import pt.ismai.ac.final2.databinding.ActivityRegisterBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterActivity : AppCompatActivity() {
    private lateinit var btn_signup: Button
    private lateinit var btn_back: Button
    private lateinit var txt_user: EditText
    private lateinit var txt_pass1: EditText
    private lateinit var txt_pass2: EditText

    // ------------------------------ Funcs ------------------------------
    fun performAuth(){
        val emailPattern = Regex(pattern = "^[A-Za-z0-9+_.-]+@(.+)\$")
        val email = txt_user.text.toString()
        val pass1 = txt_pass1.text.toString()
        val pass2 = txt_pass2.text.toString()
        val fireAuth = FirebaseAuth.getInstance()

        FirebaseApp.initializeApp(this)
        // Verificações da password e email
        if (!email.matches(emailPattern)){
            MotionToast.darkColorToast(this,
                "Invalid Email!",
                "Please use a valid Email.",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        } else if (pass1.isEmpty() || pass1.isBlank() || pass1.length < 6){
            MotionToast.darkColorToast(this,
                "Invalid Password!",
                "The password should have more than 6 characters.",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        } else if (!pass1.equals(pass2)){
            MotionToast.darkColorToast(this,
                "The passwords don't match!",
                "Passwords should match.",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        } else {
            MotionToast.darkColorToast(this,
                "Signin Up...",
                "Please wait.",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

            fireAuth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Criar user
                    val fireUser = fireAuth.currentUser
                    // Mudar atividade para login
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.putExtra("success", "sucess") // Enviar String extra
                    startActivity(intent)
                } else {
                    MotionToast.darkColorToast(
                        this,
                        "Sign Up Error.",
                        "Occurred an error during Sign Up.",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }
        }
    }

    // ---------------------------- Fim Funcs ----------------------------



    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Atribuições
        btn_signup = binding.signupSignup
        btn_back = binding.signupBack
        txt_user = binding.signupEmail
        txt_pass1 = binding.signupPassword1
        txt_pass2 = binding.signupPassword2

        // Inicio Gradiente
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#6200ee"), Color.parseColor("#13012C"))
        )
        val rootView: View = findViewById(android.R.id.content)
        rootView.background = gradient
        // Fim Gradiente

        // On Login
        btn_signup.setOnClickListener {
            performAuth();
        }
        btn_back.setOnClickListener {
            finish()
        }
    }
}