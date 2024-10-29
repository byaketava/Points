package by.byak.points

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import by.byak.points.models.Route
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class RoutesFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var routeNameField: EditText
    private lateinit var startPointField: EditText
    private lateinit var endPointField: EditText
    private lateinit var stopsField: EditText
    private lateinit var saveButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_route, container, false)

        routeNameField = view.findViewById(R.id.routeNameField)
        startPointField = view.findViewById(R.id.startPointField)
        endPointField = view.findViewById(R.id.endPointField)
        stopsField = view.findViewById(R.id.stopsField)
        saveButton = view.findViewById(R.id.saveButton)

        //bottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        //bottomNavigationView.visibility = View.VISIBLE

        saveButton.setOnClickListener {
            saveRoute()
        }

        return view
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
                requireContext(),
                "Маршрут успешно сохранен с ID: ${documentReference.id}",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(), "Ошибка при сохранении маршрута: ${e.message}", Toast.LENGTH_SHORT
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