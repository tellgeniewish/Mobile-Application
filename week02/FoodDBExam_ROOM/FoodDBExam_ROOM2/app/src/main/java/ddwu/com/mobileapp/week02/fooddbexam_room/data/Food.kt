package ddwu.com.mobileapp.week02.fooddbexam_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "food_table") // 자동으로 테이블을 만든다 // 속성을 사용하지 않으면 data class 이름으로 테이블이 생긴다
data class Food( // @Entity만 사용하면 Food 테이블이 생긴다
    @PrimaryKey (autoGenerate = true) // 자동으로 카운팅하면서 넘어간다
    val _id: Int, // 0을 넣어줘야 함!

    var food: String,
    var country: String
) // data 클래스에서 val이나 var로 선언되면 멤버변수가 됨
// data 클래스는 값을 표현할 수 있다
// equal, toString 등을 사용 ㄱㄴ
