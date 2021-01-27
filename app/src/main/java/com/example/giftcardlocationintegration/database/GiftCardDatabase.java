package com.example.giftcardlocationintegration.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.giftcardlocationintegration.Giftcard;

@Database(entities = {Giftcard.class}, version = 1)
@TypeConverters({GiftCardTypeConvertors.class})
public abstract class GiftCardDatabase extends RoomDatabase {

    public abstract GiftCardDao giftCardDao();
}
