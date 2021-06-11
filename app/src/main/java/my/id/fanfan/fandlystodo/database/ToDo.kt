package my.id.fanfan.fandlystodo.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo")
@Parcelize()
data class ToDo (@PrimaryKey(autoGenerate = true)val id:Long?,
                 @ColumnInfo(name = "title")val title:String,
                 @ColumnInfo(name = "content")val content:String,
                 @ColumnInfo(name = "date_create")val dateCreate:String?,
                 @ColumnInfo(name = "date_deadline")val dateDeadline:String?): Parcelable