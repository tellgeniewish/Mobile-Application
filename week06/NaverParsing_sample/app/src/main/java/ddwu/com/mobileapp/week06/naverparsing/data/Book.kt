package ddwu.com.mobileapp.week06.naverparsing.data

data class Book (
    var title: String?,
    var author: String?,
    var publisher: String?,
    var img: String?
) {
    override fun toString(): String {
        //return super.toString()
        return "[$title ($author)]"
    }
}