package ddwu.com.mobileapplication.finalproject.data.network

// NaverRoot
data class NaverRoot(
//    val lastBuildDate: String,
//    val total: Long,
//    val start: Long,
//    val display: Long,
    val items: List<NVLoc>,//List<Item>,
)

// NVLoc dto (item 저장)
data class NVLoc(
    val title: String,
//    val link: String,
    val address: String,
//    val description: String,
) {
    override fun toString(): String {
        return "$title - $address"
    }
}



// Book의 toString() 참고
/*
    override fun toString(): String {
        return "$title - $author"
    }
*/