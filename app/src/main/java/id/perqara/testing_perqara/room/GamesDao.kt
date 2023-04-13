package id.perqara.testing_perqara.room

import androidx.room.*

@Dao
interface GamesDao {
    @get:Query("Select * from games")
    val getGamesList: List<Games>

    @Insert
    fun insertGames(games: Games)

    @Update
    fun updateGames(games: Games)

    @Delete
    fun deleteGames(games: Games)
}