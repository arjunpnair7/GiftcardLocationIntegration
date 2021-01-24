package com.example.giftcardlocationintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GiftCardAdapter giftCardAdapter;
    private RecyclerView giftCardRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_usercards);

        giftCardRecyclerView = findViewById(R.id.userCardsRecyclerView);
        giftCardAdapter = new GiftCardAdapter(generateTestingList(), getLayoutInflater());


        giftCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftCardRecyclerView.setAdapter(giftCardAdapter);


    }






//Temporary method created to generate a testing list, will later be replaced with actual user data
    private List<Giftcard> generateTestingList() {
        List<Giftcard> testList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            testList.add(new Giftcard("Name: " + i, "Balance: " + i, "Expiration: " + i));
        }
        return testList;
    }
}