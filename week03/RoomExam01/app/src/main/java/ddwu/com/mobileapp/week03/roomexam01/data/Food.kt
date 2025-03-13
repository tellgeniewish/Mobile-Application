package ddwu.com.mobile.roomexam01.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey (autoGenerate = true)
    val _id: Int, // val이라서 한 번 지정되면 바꿀 수 없다

    var food: String?,

    var country: String?
) {
    // override toString()
    override fun toString(): String {
        return "$_id - $food ($country)"
    }
}
