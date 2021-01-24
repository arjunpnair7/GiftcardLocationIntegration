package com.example.giftcardlocationintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GiftCardListFragment.NewCardCallback {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get current fragment and make sure that it is not null
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        //Create a new fragment if fragment is null
        if (currentFragment == null) {
            currentFragment = GiftCardListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();
        }




    }


    @Override
    public void addNewCardClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, NewGiftCardFragment.newInstance()).addToBackStack(null).commit();
    }
}