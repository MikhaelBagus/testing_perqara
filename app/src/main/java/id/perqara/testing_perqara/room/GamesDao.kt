package id.perqara.testing_perqara.room

import androidx.room.*

@Dao
interface GamesDao {
    @get:Query("SELECT * FROM games")
    val gamesList: List<Games?>?

    @Query("SELECT * FROM games WHERE id_games = :id_games")
    fun getGamesDetail(id_games: Int?): Games?

    @Insert
    fun insertGames(games: Games?)

    @Update
    fun updateGames(games: Games?)

    @Delete
    fun deleteGames(games: Games?)

    @Query("DELETE FROM games")
    fun deleteAll()
}
