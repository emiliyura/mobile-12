package com.example.mobile12;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;

// Определение контент-провайдера для работы с базой данных книг
public class BookProvider extends ContentProvider {
    // Объект SQLiteDatabase для работы с базой данных
    private SQLiteDatabase db;

    // URI контент-провайдера
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.mobile12.provider/books");

    // Создание контент-провайдера
    @Override
    public boolean onCreate() {
        // Создание объекта DatabaseHelper для работы с базой данных
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        // Получение объекта SQLiteDatabase для работы с базой данных
        db = dbHelper.getWritableDatabase();
        // Возвращение true, если объект SQLiteDatabase не равен null
        return (db != null);
    }

    // Запрос данных из базы данных
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Выполнение запроса к таблице "books" с указанными параметрами
        return db.query("books", projection, selection, selectionArgs, null, null, sortOrder);
    }

    // Вставка новой записи в базу данных
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Вставка новой записи в таблицу "books" с указанными значениями
        long rowID = db.insert("books", "", values);
        // Создание URI новой записи с указанным идентификатором
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        // Оповещение контент-резолвера об изменении данных
        getContext().getContentResolver().notifyChange(_uri, null);
        // Возвращение URI новой записи
        return _uri;
    }

    // Удаление записи из базы данных
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Удаление записи из таблицы "books" с указанными параметрами
        int count = db.delete("books", selection, selectionArgs);
        // Оповещение контент-резолвера об изменении данных
        getContext().getContentResolver().notifyChange(uri, null);
        // Возвращение количества удаленных записей
        return count;
    }

    // Обновление записи в базе данных
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Обновление записи в таблице "books" с указанными параметрами
        int count = db.update("books", values, selection, selectionArgs);
        // Оповещение контент-резолвера об изменении данных
        getContext().getContentResolver().notifyChange(uri, null);
        // Возвращение количества обновленных записей
        return count;
    }

    // Возвращение типа MIME данных
    @Override
    public String getType(Uri uri) {
        // Возвращение типа MIME данных для таблицы "books"
        return "vnd.android.cursor.dir/vnd.example.books";
    }
}
