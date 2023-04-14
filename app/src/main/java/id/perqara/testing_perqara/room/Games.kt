package id.perqara.testing_perqara.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "id_games")
    var idGames: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "released")
    var released: String? = null,

    @ColumnInfo(name = "background_image")
    var backgroundImage: String? = null,

    @ColumnInfo(name = "rating")
    var rating: Double? = null,

    @ColumnInfo(name = "publishers_name")
    var publishersName: String? = null,

    @ColumnInfo(name = "playtime")
    var playtime: Int? = null
)

