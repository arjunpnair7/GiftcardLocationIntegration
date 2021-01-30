package com.example.giftcardlocationintegration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class GiftCardViewModel extends ViewModel {

    LiveData<List<Giftcard>> myGiftCards = GiftCardListFragment.giftCardDatabase.giftCardDao().getAllGiftCards();
    LiveData<List<String>> currentGiftCardNames = GiftCardListFragment.giftCardDatabase.giftCardDao().getCurrentGiftCardNames();
}
