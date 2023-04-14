package id.perqara.testing_perqara.room

import androidx.room.*

@Dao
interface GamesDao {
    @get:Query("Select * from games")
    val gamesList: List<Games?>?

    @Query("Select * from games where id_games = :id_games")
    fun getGamesDetail(id_games: Int?): Games?

    @Insert
    fun insertGames(games: Games?)

    @Update
    fun updateGames(games: Games?)

    @Delete
    fun deleteGames(games: Games?)
}
