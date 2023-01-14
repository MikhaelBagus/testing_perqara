package id.perqara.testing_perqara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.perqara.testing_perqara.data.model.GamesModel;
import id.perqara.testing_perqara.data.model.PublishersGamesModel;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "testing_perqara";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "favorite_games";

    private static final String ID_COL = "id";
    private static final String ID_GAMES_COL = "id_games";
    private static final String NAME_COL = "name";
    private static final String DESCRIPTION_COL = "description";
    private static final String RELEASED_COL = "released";
    private static final String BACKGROUND_IMAGE_COL = "background_image";
    private static final String RATING_COL = "rating";
    private static final String PUBLISHERS_NAME_COL = "publishers_name";
    private static final String PLAYTIME_COL = "playtime";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_GAMES_COL + " INTEGER,"
                + NAME_COL + " STRING,"
                + DESCRIPTION_COL + " STRING,"
                + RELEASED_COL + " STRING,"
                + BACKGROUND_IMAGE_COL + " STRING,"
                + RATING_COL + " DOUBLE,"
                + PUBLISHERS_NAME_COL + " STRING,"
                + PLAYTIME_COL + " INTEGER)";

        db.execSQL(query);
    }

    public void addNewFavorite(Integer idGames, String name, String description, String released, String backgroundImage, Double rating, String publisherName, Integer playtime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ID_GAMES_COL, idGames);
        values.put(NAME_COL, name);
        values.put(DESCRIPTION_COL, description);
        values.put(RELEASED_COL, released);
        values.put(BACKGROUND_IMAGE_COL, backgroundImage);
        values.put(RATING_COL, rating);
        values.put(PUBLISHERS_NAME_COL, publisherName);
        values.put(PLAYTIME_COL, playtime);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<GamesModel> readFavorite() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorGames = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<GamesModel> gamesArrayList = new ArrayList<>();

        if (cursorGames.moveToFirst()) {
            do {
                ArrayList<PublishersGamesModel> publishersGamesArrayList = new ArrayList<>();
                publishersGamesArrayList.add(new PublishersGamesModel(cursorGames.getInt(1), cursorGames.getString(7)));

                gamesArrayList.add(new GamesModel(cursorGames.getInt(1), cursorGames.getString(2), cursorGames.getString(3), cursorGames.getString(4), cursorGames.getString(5), cursorGames.getDouble(6), publishersGamesArrayList, cursorGames.getInt(8)));
            } while (cursorGames.moveToNext());
        }
        cursorGames.close();
        return gamesArrayList;
    }

    public void updateFavorite(Integer idGames, String name, String description, String released, String backgroundImage, Double rating, String publisherName, Integer playtime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID_GAMES_COL, idGames);
        values.put(NAME_COL, name);
        values.put(DESCRIPTION_COL, description);
        values.put(RELEASED_COL, released);
        values.put(BACKGROUND_IMAGE_COL, backgroundImage);
        values.put(RATING_COL, rating);
        values.put(PUBLISHERS_NAME_COL, publisherName);
        values.put(PLAYTIME_COL, playtime);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_NAME, values, "id_games=?", new String[]{idGames.toString()});
        db.close();
    }

    public void deleteFavorite(Integer idGames) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "id_games=?", new String[]{idGames.toString()});
        db.close();
    }

    public GamesModel readDetailFavorite(Integer idGames) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorGames = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id_games=" + idGames.toString() , null);

        GamesModel gamesDetail = null;
        if (cursorGames.moveToFirst()) {
            do {
                ArrayList<PublishersGamesModel> publishersGamesArrayList = new ArrayList<>();
                publishersGamesArrayList.add(new PublishersGamesModel(cursorGames.getInt(1), cursorGames.getString(7)));

                gamesDetail = new GamesModel(cursorGames.getInt(1), cursorGames.getString(2), cursorGames.getString(3), cursorGames.getString(4), cursorGames.getString(5), cursorGames.getDouble(6), publishersGamesArrayList, cursorGames.getInt(8));
            } while (cursorGames.moveToNext());
        }
        cursorGames.close();
        return gamesDetail;
    }
}

