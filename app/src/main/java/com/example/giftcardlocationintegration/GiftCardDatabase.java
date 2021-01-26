package com.example.giftcardlocationintegration;

import androidx.room.RoomDatabase;

public abstract class GiftCardDatabase extends RoomDatabase {

    public abstract GiftCardDao giftCardDao();
}
