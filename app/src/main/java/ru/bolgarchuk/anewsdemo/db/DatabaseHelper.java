package ru.bolgarchuk.anewsdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.bolgarchuk.anewsdemo.model.TopNews;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public final static String TAG = DatabaseHelper.class.getName();
    private static final String DATABASE_NAME = "ru.bolgarchuk.anewsdemo.topnews.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<TopNews, Integer> mTopNewsDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, TopNews.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<TopNews, Integer> getNewsDao() throws SQLException {
        if (mTopNewsDao == null) {
            mTopNewsDao = getDao(TopNews.class);
        }
        return mTopNewsDao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade, newVersion: " + newVersion);
            TableUtils.dropTable(connectionSource, TopNews.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clearNewsTable() {
        try {
            TableUtils.clearTable(getConnectionSource(), TopNews.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        mTopNewsDao = null;
    }
}
