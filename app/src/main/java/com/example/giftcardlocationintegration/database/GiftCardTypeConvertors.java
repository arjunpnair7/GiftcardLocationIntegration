package com.example.giftcardlocationintegration.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class GiftCardTypeConvertors {

    @TypeConverter
    public Long fromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date toDate(Long input) {
        return new Date(input);
    }

}
