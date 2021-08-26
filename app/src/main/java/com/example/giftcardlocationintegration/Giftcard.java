package com.example.giftcardlocationintegration;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Giftcard {

    @PrimaryKey(autoGenerate = true)
    public int identifier;

    public String cardName;
    public float cardBalance;
    public Date cardExpiration;
    public String cardBarcodeNumber;
    public int cardPinCode;

    public Giftcard(String cardName, float cardBalance, Date cardExpiration, String cardBarcodeNumber, int cardPinCode) {
        this.cardName = cardName;
        this.cardBalance = cardBalance;
        this.cardExpiration = cardExpiration;
        this.cardBarcodeNumber = cardBarcodeNumber;
        this.cardPinCode = cardPinCode;
    }


}
