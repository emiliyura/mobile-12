package com.example.mobile12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;

// Класс для работы с базой данных
public class DatabaseHelper extends SQLiteOpenHelper {
    // Определяем названия таблицы и столбцов
    private static final String TABLE_NAME = "books";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";

    // Конструктор класса
    public DatabaseHelper(Context context) {
        super(context, "books.db", null, 1);
    }

    // Создаем таблицу при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT)";
        db.execSQL(createTable);
    }

    // Обновляем таблицу при изменении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавляем новую книгу в таблицу
    public boolean addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, book.getTitle());
        cv.put(COLUMN_AUTHOR, book.getAuthor());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    // Удаляем книгу из таблицы по автору
    public boolean deleteBook(String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_AUTHOR + " = ?", new String[]{author});
        db.close();
        return result > 0;
    }

    // Находим книгу в таблице по автору
    public Book findBook(String author) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_AUTHOR},
                COLUMN_AUTHOR + " = ?", new String[]{author}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return book;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    // Получаем все книги из таблицы
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    // Обновляем книгу в таблице по автору
    public boolean updateBook(String oldAuthor, String newTitle, String newAuthor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, newTitle);
        cv.put(COLUMN_AUTHOR, newAuthor);
        // Обновляем запись в таблице, где автор совпадает с oldAuthor
        int result = db.update(TABLE_NAME, cv, COLUMN_AUTHOR + " = ?", new String[]{oldAuthor});
        db.close();
        return result > 0;
    }
}
