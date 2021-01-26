package com.example.giftcardlocationintegration;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Giftcard {

    @PrimaryKey(autoGenerate = true)
    public int identifier;

    public String cardName;
    public String cardBalance;
    public Date cardExpiration;
    public int cardBarcodeNumber;
    public int cardPinCode;

    public Giftcard(String cardName, String cardBalance, Date cardExpiration, int cardBarcodeNumber, int cardPinCode) {
        this.cardName = cardName;
        this.cardBalance = cardBalance;
        this.cardExpiration = cardExpiration;
        this.cardBarcodeNumber = cardBarcodeNumber;
        this.cardPinCode = cardPinCode;
    }


}
