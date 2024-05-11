package com.example.mobile12;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов пользовательского интерфейса
        EditText titleInput = findViewById(R.id.title_input); // поле ввода названия книги
        EditText authorInput = findViewById(R.id.author_input); // поле ввода автора книги
        Button saveButton = findViewById(R.id.save_button); // кнопка сохранения книги
        Button deleteButton = findViewById(R.id.delete_button); // кнопка удаления книги
        Button findButton = findViewById(R.id.find_button); // кнопка поиска книги
        Button task2Button = findViewById(R.id.activity2goto); // кнопка перехода на другую активность
        RecyclerView booksList = findViewById(R.id.books_list); // список книг

        // Инициализация помощника базы данных и получение всех книг
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Book> books = dbHelper.getAllBooks();

        // Инициализация адаптера книг и установка его для RecyclerView
        BookAdapter adapter = new BookAdapter((ArrayList<Book>) books);
        booksList.setLayoutManager(new LinearLayoutManager(this));
        booksList.setAdapter(adapter);

        // Обработчик нажатия кнопки сохранения новой книги
        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString(); // получение названия книги
            String author = authorInput.getText().toString(); // получение автора книги
            if (dbHelper.addBook(new Book(0, title, author))) { // добавление книги в базу данных
                books.add(new Book(0, title, author)); // добавление книги в список
                adapter.notifyItemInserted(books.size() - 1); // уведомление адаптера о добавлении новой книги
                Toast.makeText(this, "Книга сохранена успешно!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Не удалось сохранить книгу", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия кнопки перехода на другую активность
        task2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Task2Activity.class);
                startActivity(intent);
            }
        });

        // Обработчик нажатия кнопки удаления книги
        deleteButton.setOnClickListener(v -> {
            String author = authorInput.getText().toString(); // получение автора книги для удаления
            if (dbHelper.deleteBook(author)) { // удаление книги из базы данных
                int position = -1;
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getAuthor().equals(author)) {
                        position = i;
                        books.remove(i); // удаление книги из списка
                        break;
                    }
                }
                if (position != -1) {
                    adapter.notifyItemRemoved(position); // уведомление адаптера об удалении книги
                    Toast.makeText(this, "Книга удалена успешно!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Не удалось удалить книгу", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия кнопки поиска книги по автору
        findButton.setOnClickListener(v -> {
            String author = authorInput.getText().toString(); // получение автора для поиска
            Book foundBook = dbHelper.findBook(author); // поиск книги в базе данных
            if (foundBook != null) {
                titleInput.setText(foundBook.getTitle()); // установка названия найденной книги
                authorInput.setText(foundBook.getAuthor()); // установка автора найденной книги
                Toast.makeText(this, "Книга найдена: " + foundBook.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия кнопки обновления книги
        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> {
            String oldAuthor = authorInput.getText().toString(); // получение старого автора для поиска
            String newTitle = titleInput.getText().toString(); // получение нового названия для обновления
            String newAuthor = authorInput.getText().toString(); // получение нового автора для обновления
            if (dbHelper.updateBook(oldAuthor, newTitle, newAuthor)) { // обновление книги в базе данных
                Toast.makeText(this, "Книга обновлена успешно!", Toast.LENGTH_SHORT).show();
                // Обновление списка и адаптера
                refreshBooksList(dbHelper, books, adapter, booksList);
            } else {
                Toast.makeText(this, "Не удалось обновить книгу", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Метод для обновления списка книг после изменения в базе данных
    private void refreshBooksList(DatabaseHelper dbHelper,
                                  List<Book> books, BookAdapter adapter, RecyclerView
                                          booksList) {
        books = dbHelper.getAllBooks(); // получение обновленного списка книг
        adapter = new BookAdapter((ArrayList<Book>) books); // создание нового адаптера
        booksList.setAdapter(adapter); // установка нового адаптера для RecyclerView
    }
}
