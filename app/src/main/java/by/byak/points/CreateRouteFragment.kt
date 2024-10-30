package by.byak.points

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style


class CreateRouteFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_route, container, false)
        mapView = view.findViewById(R.id.mapView)

        mapView.getMapboxMap().apply {
            loadStyleUri(Style.MAPBOX_STREETS) {
                setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(LONGITUDE_MINSK, LATITUDE_MINSK))
                        .zoom(12.0)
                        .build()
                )
            }
        }

        return view
    }



    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }


    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    companion object {
        private const val LATITUDE_MINSK = 53.9
        private const val LONGITUDE_MINSK = 27.5667
    }
}
