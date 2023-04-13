package id.perqara.testing_perqara.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
class Games {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null

    @ColumnInfo(name = "id_games")
    val id_games: Int? = null

    @ColumnInfo(name = "name")
    val name: String? = null

    @ColumnInfo(name = "description")
    val description: String? = null

    @ColumnInfo(name = "released")
    val released: String? = null

    @ColumnInfo(name = "background_image")
    val background_image: String? = null

    @ColumnInfo(name = "rating")
    val rating: Double? = null

    @ColumnInfo(name = "publishers_name")
    val publishers_name: String? = null

    @ColumnInfo(name = "playtime")
    val playtime: Int? = null
}
