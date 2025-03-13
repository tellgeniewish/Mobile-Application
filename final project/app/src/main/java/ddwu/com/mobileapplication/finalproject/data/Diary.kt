package ddwu.com.mobileapplication.finalproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "diary_table")
data class Diary(
    @PrimaryKey (autoGenerate = true)
    val _id: Int,

    @ColumnInfo (name="diary")
    var name: String?,

    var year: Int,
    var month:Int,
    var day:Int,

    var memo: String?
) : Serializable {
    // override toString()
    override fun toString(): String {
        return "$name ($year-$month-$day)"
    }
}