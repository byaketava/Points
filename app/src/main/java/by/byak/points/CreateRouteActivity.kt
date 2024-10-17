package by.byak.points

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.byak.points.models.Route
import com.google.firebase.firestore.FirebaseFirestore

class CreateRouteActivity : AppCompatActivity() {
    private lateinit var routeNameField: EditText
    private lateinit var startPointField: EditText
    private lateinit var endPointField: EditText
    private lateinit var stopsField: EditText
    private lateinit var saveButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)

        routeNameField = findViewById(R.id.routeNameField)
        startPointField = findViewById(R.id.startPointField)
        endPointField = findViewById(R.id.endPointField)
        stopsField = findViewById(R.id.stopsField)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            saveRoute()
        }

        // Загружаем маршруты при запуске Activity
        // loadRoutes()
    }

    private fun saveRoute() {
        val routeName = routeNameField.text.toString()
        val startPoint = startPointField.text.toString()
        val endPoint = endPointField.text.toString()
        val stops = stopsField.text.toString().split(",").map { it.trim() } // Удаляем пробелы

        // Создаем объект Route
        val route = Route(routeName, startPoint, endPoint, stops)

        // Сохраняем объект в Firestore
        db.collection("routes").add(route).addOnSuccessListener { documentReference ->
                Toast.makeText(
                    this,
                    "Маршрут успешно сохранен с ID: ${documentReference.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this, "Ошибка при сохранении маршрута: ${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
    }

    /*private fun loadRoutes() {
        db.collection("routes")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val route = document.toObject(Route::class.java)
                    // Добавьте маршрут в список или отобразите его на экране
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка загрузки маршрутов: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }*/
}