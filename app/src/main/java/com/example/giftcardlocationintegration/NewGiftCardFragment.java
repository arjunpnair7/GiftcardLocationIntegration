package com.example.giftcardlocationintegration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewGiftCardFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View giftCardFragmentLayout = inflater.inflate(R.layout.fragment_newgiftcard, container, false);
        return giftCardFragmentLayout;

    }

    public static NewGiftCardFragment newInstance() {
        return new NewGiftCardFragment();
    }
}
