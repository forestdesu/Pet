package com.example.pet;

import android.os.Bundle;
import android.view.View;
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
    ProgressBar loadingPB;

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
        // calling a method to post the data and passing our name and job.
        postData(Id.getText().toString(), Integer.parseInt(CategoryId.getText().toString()), CategoryName.getText().toString(), Name.getText().toString(),
                PhotoUrl.getText().toString(), Integer.parseInt(TagsId.getText().toString()), TagsName.getText().toString(), Status.getText().toString());
    }

    private void postData(String id, Integer categoryId, String categoryName, String name, String photoUrl, Integer tagsId, String tagsName, String status) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://petstore.swagger.io/v2/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        PetAPI petAPI = retrofit.create(PetAPI.class);

        // passing data from our text fields to our modal class.
        Pet modal = new Pet(id, new Category(categoryId, categoryName), name, photoUrl, List<new Tags(tagsId, tagsName)>, status);

        // calling a method to create a post and passing our modal class.
        Call<Pet> call = petAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.
                jobEdt.setText("");
                nameEdt.setText("");

                // we are getting response from our body
                // and passing it to our modal class.
                Pet responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getJob();

                // below line we are setting our
                // string to our text view.
                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}