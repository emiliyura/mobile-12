package com.example.mobile12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    // Список книг для отображения в RecyclerView
    private ArrayList<Book> books;

    // Статический класс ViewHolder для хранения ссылок на элементы разметки
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Текстовые поля для отображения названия и автора книги
        private final TextView titleTextView;
        private final TextView authorTextView;

        // Конструктор ViewHolder, принимающий View элемент разметки
        public ViewHolder(View view) {
            super(view);
            // Инициализация текстовых полей из элементов разметки
            titleTextView = view.findViewById(R.id.book_title);
            authorTextView = view.findViewById(R.id.book_author);
        }

        // Метод bind для установки значений текстовых полей из объекта Book
        public void bind(Book book) {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
        }
    }

    // Конструктор BookAdapter, принимающий список книг
    public BookAdapter(ArrayList<Book> books) {
        if (books == null) {
            throw new IllegalArgumentException("Books list cannot be null");
        }
        this.books = books;
    }

    // Метод onCreateViewHolder для создания нового ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового View элемента разметки с помощью LayoutInflater
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        // Возвращение нового ViewHolder с указанным View элементом
        return new ViewHolder(view);
    }

    // Метод onBindViewHolder для привязки данных к ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Привязка данных из списка книг к текущему ViewHolder
        holder.bind(books.get(position));
    }

    // Метод getItemCount для получения количества элементов в списке
    @Override
    public int getItemCount() {
        return books.size();
    }

    // Метод setBooks для установки нового списка книг
    public void setBooks(ArrayList<Book> newBooks) {
        if (newBooks == null) {
            throw new IllegalArgumentException("Books list cannot be null");
        }
        this.books = newBooks;
        // Оповещение адаптера об изменении данных
        notifyDataSetChanged();
    }
}
