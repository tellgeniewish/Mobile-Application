package ddwu.com.mobileapp.week07.naverretrofit.data.network


// BookRoot
data class BookRoot(
//    val lastBuildDate: String,
//    val total: Long,
//    val start: Long,
//    val display: Long,
    val items: List<Book>,//List<Item>,
)

// Book dto (item 저장)
data class Book(
    val title: String,
//    val link: String,
    val image: String,
    val author: String,
//    val discount: String,
    val publisher: String,
//    val pubdate: String,
//    val isbn: String,
//    val description: String,
) {
    override fun toString(): String {
        return "$title - $author"
    }
}



// Book의 toString() 참고
/*
    override fun toString(): String {
        return "$title - $author"
    }
*/

