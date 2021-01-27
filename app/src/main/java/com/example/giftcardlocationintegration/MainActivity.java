package com.example.giftcardlocationintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements GiftCardListFragment.Callbacks {

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

    @Override
    public void finishedEditCard() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, GiftCardListFragment.newInstance()).addToBackStack(null).commit();

    }
}