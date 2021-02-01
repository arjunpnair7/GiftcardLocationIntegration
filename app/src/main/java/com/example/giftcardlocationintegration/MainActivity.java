package com.example.giftcardlocationintegration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements GiftCardListFragment.Callbacks {

    private Fragment currentFragment;
    private String TAG = "MainActivity";
    public static int LOCATION_PERMISSION_CODE = 1;
    public static String CARD_NAME_KEY = "CARDNAME";
    public static String DATE_KEY = "DATE";
    public static String BALANCE_KEY = "BALANCE";
    public static String BARCODE_KEY = "BARCODE";
    public static String PINCODE_KEY = "PINCODE";

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
            GiftCardViewHolder.context = this;
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

    @Override
    public void editExistingCard(String cardName, float cardBalance, Date cardExpiration, String cardBarcodeNumber, int cardPinCode) {
        Log.i(TAG, "editExistingCard");
        NewGiftCardFragment newGiftCardFragment = NewGiftCardFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(CARD_NAME_KEY, cardName);
        bundle.putFloat(BALANCE_KEY, cardBalance);
        newGiftCardFragment.selectedDate = cardExpiration;
        bundle.putString(BARCODE_KEY, cardBarcodeNumber);
        bundle.putInt(PINCODE_KEY, cardPinCode);
        newGiftCardFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newGiftCardFragment).addToBackStack(null).commit();
    }

}