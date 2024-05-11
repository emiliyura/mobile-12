package com.example.mobile12;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Task2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);

        // Создаем экземпляр класса User
        User user = new User(1, "Kerimov Emil", "kerimov_emil@example.com");

        // Преобразуем объект User в JSON формат с помощью библиотеки Gson
        Gson gson = new Gson();
        String json = gson.toJson(user);

        // Создаем файл для сохранения JSON данных
        File file = new File(getFilesDir(), "user.json");

        // Записываем JSON данные в файл
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Читаем JSON данные из файла
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);

            // Преобразуем JSON данные из файла в объект User с помощью библиотеки Gson
            User userFromJson = gson.fromJson(isr, User.class);

            // Находим элементы TextView на экране
            TextView textViewId = findViewById(R.id.textViewId);
            TextView textViewName = findViewById(R.id.textViewName);
            TextView textViewEmail = findViewById(R.id.textViewEmail);

            // Устанавливаем значения для элементов TextView из объекта User
            textViewId.setText(String.valueOf(userFromJson.getId()));
            textViewName.setText(userFromJson.getName());
            textViewEmail.setText(userFromJson.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
