package com.example.giftcardlocationintegration;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;
import java.util.Date;

public class GiftCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    MaterialTextView giftCardStoreName = itemView.findViewById(R.id.cardNameTextView);
    MaterialTextView giftCardExpirationDate = itemView.findViewById(R.id.expirationTextView);
    MaterialTextView giftCardBalanceAmount = itemView.findViewById(R.id.balanceTextView);
    private String cardBarcodeNumber;
    private int cardPinCode;
    private float cardBalance;
    private Date cardDate;
    private String TAG = "GiftCardViewHolder";
    public static Context context;
    private Activity myActivity;
    private GiftCardListFragment.Callbacks callbacks;


    public GiftCardViewHolder(@NonNull View itemView, Activity myActivity) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.myActivity = myActivity;
    }



    //Method to update contents of view holder
    public void updateGiftCardViewHolder(Giftcard giftcard) {
        giftCardStoreName.setText(giftcard.cardName);
        giftCardBalanceAmount.setText("$" + String.valueOf(giftcard.cardBalance));
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        giftCardExpirationDate.setText(new Date().toString());
        cardBarcodeNumber = giftcard.cardBarcodeNumber;
        Log.i(TAG, "card balance: " + giftcard.cardBalance);
        cardPinCode = giftcard.cardPinCode;
        cardBalance = giftcard.cardBalance;
        cardDate = giftcard.cardExpiration;


    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "clicked a giftCard");
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
        callbacks = (GiftCardListFragment.Callbacks) myActivity;
        callbacks.editExistingCard(giftCardStoreName.getText().toString(), cardBalance, cardDate, cardBarcodeNumber , cardPinCode);
    }
}
