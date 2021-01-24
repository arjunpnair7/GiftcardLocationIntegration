package com.example.giftcardlocationintegration;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class GiftCardViewHolder extends RecyclerView.ViewHolder {
    MaterialTextView giftCardStoreName = itemView.findViewById(R.id.cardNameTextView);
    MaterialTextView giftCardExpirationDate = itemView.findViewById(R.id.expirationTextView);
    MaterialTextView giftCardBalanceAmount = itemView.findViewById(R.id.balanceTextView);


    public GiftCardViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    //Method to update contents of view holder
    public void updateGiftCardViewHolder(Giftcard giftcard) {
        giftCardStoreName.setText(giftcard.cardName);
        giftCardBalanceAmount.setText(giftcard.cardBalance);
        giftCardExpirationDate.setText(giftcard.cardExpiration);
    }
}
