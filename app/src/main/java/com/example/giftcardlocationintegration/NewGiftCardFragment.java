package com.example.giftcardlocationintegration;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class NewGiftCardFragment extends Fragment {
    private String TAG = "NewGiftCardFragment";

    private com.google.android.material.textfield.TextInputLayout merchantTextView;
    private com.google.android.material.textfield.TextInputLayout barcodeTextView;
    private com.google.android.material.textfield.TextInputLayout pincodeTextView;
    private com.google.android.material.textfield.TextInputLayout expirationTextView;
    private com.google.android.material.textfield.TextInputLayout balanceTextView;


    private FloatingActionButton confirmationFab;
    private GiftCardListFragment.NewCardCallback callback;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View giftCardFragmentLayout = inflater.inflate(R.layout.fragment_newgiftcard, container, false);

        confirmationFab = giftCardFragmentLayout.findViewById(R.id.confirmationFab);
        merchantTextView = giftCardFragmentLayout.findViewById(R.id.enterMerchantField);
        barcodeTextView = giftCardFragmentLayout.findViewById(R.id.enterBarcodeField);
        pincodeTextView = giftCardFragmentLayout.findViewById(R.id.enterPinCodeField);
        expirationTextView = giftCardFragmentLayout.findViewById(R.id.enterExpirationDate);
        balanceTextView = giftCardFragmentLayout.findViewById(R.id.enterBalance);


        confirmationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.finishedEditCard();
                Log.i(TAG, "Current info: " + merchantTextView.getEditText().getText() +
                        barcodeTextView.getEditText().getText() +
                        pincodeTextView.getEditText().getText() +
                        expirationTextView.getEditText().getText() +
                        balanceTextView.getEditText().getText());
            }
        });


        return giftCardFragmentLayout;

    }

    public static NewGiftCardFragment newInstance() {
        return new NewGiftCardFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (GiftCardListFragment.NewCardCallback) context;
    }


}
