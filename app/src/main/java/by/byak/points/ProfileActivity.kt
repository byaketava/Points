package by.byak.points

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var userNameText: TextView
    private lateinit var userEmailText: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        userNameText = findViewById(R.id.userNameText)
        userEmailText = findViewById(R.id.userEmailText)
        logoutButton = findViewById(R.id.logoutButton)

        // Получаем текущего пользователя
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("username")
                        val email = document.getString("email")

                        userNameText.text = username ?: "Неизвестный пользователь"
                        userEmailText.text = email ?: "Неизвестный email"
                    } else {
                        Toast.makeText(
                            this,
                            "Ошибка загрузки данных пользователя",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Настраиваем кнопку выхода
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }
}
