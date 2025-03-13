package ddwu.com.mobileapplication.finalproject.data

data class TMapRoot(
    val searchPoiInfo: SearchPoiInfo,
)

data class SearchPoiInfo(
    val totalCount: Int,
    val count: Int,
    val page: Int,
    val pois: Pois,
)

data class Pois(
    val poi: List<Poi>,
)

data class Poi(
    val id: String,
    val name: String,
    val noorLat: Double,
    val noorLon: Double,
    val telNo: String
)