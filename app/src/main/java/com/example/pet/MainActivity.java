package com.example.pet;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView Answer;
    EditText Id;
    EditText CategoryId;
    EditText CategoryName;
    EditText Name;
    EditText PhotoUrl;
    EditText TagsId;
    EditText TagsName;
    EditText Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Answer = (TextView) findViewById(R.id.answer);
        Id = (EditText) findViewById(R.id.id);
        CategoryId = (EditText) findViewById(R.id.category_id);
        CategoryName = (EditText) findViewById(R.id.category_name);
        Name = (EditText) findViewById(R.id.name);
        PhotoUrl = (EditText) findViewById(R.id.photo_url);
        TagsId = (EditText) findViewById(R.id.tags_id);
        TagsName = (EditText) findViewById(R.id.tags_name);
        Status = (EditText) findViewById(R.id.status);
    }

    public void onClick(View view) {
        if (Id.getText().toString().isEmpty() && CategoryId.getText().toString().isEmpty() && CategoryName.getText().toString().isEmpty() &&
                Name.getText().toString().isEmpty() && PhotoUrl.getText().toString().isEmpty() && TagsId.getText().toString().isEmpty() &&
                TagsName.getText().toString().isEmpty() && Status.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Пожалуйста заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }
        postData(Id.getText().toString(), Integer.parseInt(CategoryId.getText().toString()), CategoryName.getText().toString(), Name.getText().toString(),
                PhotoUrl.getText().toString(), Integer.parseInt(TagsId.getText().toString()), TagsName.getText().toString(), Status.getText().toString());
    }

    private void postData(String id, Integer categoryId, String categoryName, String name, String photoUrl, Integer tagsId, String tagsName, String status) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://petstore.swagger.io/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PetAPI petAPI = retrofit.create(PetAPI.class);
        ArrayList<Tags> tags = new ArrayList<>();
        Tags tag = new Tags(tagsId, tagsName);
        tags.add(tag);

        ArrayList<String> urls = new ArrayList<>();
        urls.add(photoUrl);
        Pet modal = new Pet(Integer.parseInt(id), new Category(categoryId, categoryName), name, urls, tags, status);
        Call<Pet> call = petAPI.createPost(modal);
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                String responseString = "Response Code : " + response.code();
                Answer.setText(responseString);
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Answer.setText("Error found is : " + t.getMessage());
            }
        });
    }
}