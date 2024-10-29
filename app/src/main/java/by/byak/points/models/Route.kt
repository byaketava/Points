package by.byak.points.models

data class Route(
    val routeName: String, val startPoint: String, val endPoint: String, val stops: List<String>
)