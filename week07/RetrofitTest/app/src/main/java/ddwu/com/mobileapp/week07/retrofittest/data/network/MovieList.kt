package ddwu.com.mobileapp.week07.retrofittest.data.network

import com.google.gson.annotations.SerializedName

/*data class 를 사용하여 DTO 작성*/
data class Root(
    //val boxOfficeResult: BoxOfficeResult,
    @SerializedName("boxOfficeResult") val movieResult: BoxOfficeResult,
)

data class BoxOfficeResult(
    val boxofficeType: String, // 필요없으면 지워도 됨
    val showRange: String,
    //val dailyBoxOfficeList: List<Movie>, // key값랑 멤버변수는 같아야 함 --> annotation으로 변경 ㄱㄴ(컴파일러용 주석)
    @SerializedName("dailyBoxOfficeList") val movieList: List<Movie>,
)

data class Movie(
    //val rnum: String,
    val rank: String,
//    val rankInten: String,
//    val rankOldAndNew: String,
//    val movieCd: String,
    //val movieNm: String,
    @SerializedName("movieNm") val title: String,
    //val openDt: String,,
    @SerializedName("openDt") val openDate: String,
//    val salesAmt: String,
//    val salesShare: String,
//    val salesInten: String,
//    val salesChange: String,
//    val salesAcc: String,
//    val audiCnt: String,
//    val audiInten: String,
//    val audiChange: String,
//    val audiAcc: String,
//    val scrnCnt: String,
//    val showCnt: String,
)

