package ddwu.com.mobileapp.week06.naverparsing.data.network

import android.content.Context
import ddwu.com.mobileapp.week06.naverparsing.R
import ddwu.com.mobileapp.week06.naverparsing.data.Book
import ddwu.com.mobileapp.week06.naverparsing.data.network.util.NaverBookParser
import ddwu.com.mobileapp.week06.naverparsing.data.network.util.NetworkUtil

class NetworkService(private val context: Context) {

    fun getBooksByKeyword(keyword: String) : List<Book> {
        val address : String = context.resources.getString(R.string.naver_url)

        val params = HashMap<String, String>()
        params["query"] = keyword
        //params["title"] = context.resources.getString(R.string.client_id)
        //params["title"] = keyword

        val result = try {
            NetworkUtil(context).sendRequest(NetworkUtil.GET, address, params)
        } catch(e: Exception) {
            e.printStackTrace()
            null
        }

        return NaverBookParser().parse(result)
    }

}