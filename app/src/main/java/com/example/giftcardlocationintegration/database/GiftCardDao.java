package com.example.giftcardlocationintegration.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.giftcardlocationintegration.Giftcard;

import java.util.List;

@Dao
public interface GiftCardDao {

    @Insert
    public void addNewGiftCard(Giftcard giftCard);

    @Query("SELECT * FROM giftcard")
    public LiveData<List<Giftcard>> getAllGiftCards();

    @Query("SELECT cardName FROM giftcard")
    public LiveData<List<String>> getCurrentGiftCardNames();
}
