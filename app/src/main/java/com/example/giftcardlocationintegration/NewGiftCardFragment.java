package com.example.giftcardlocationintegration;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewGiftCardFragment extends Fragment implements DatePickerFragment.DatePickerCallback {
    private String TAG = "NewGiftCardFragment";

    private com.google.android.material.textfield.TextInputLayout merchantTextView;
    private com.google.android.material.textfield.TextInputLayout barcodeTextView;
    private com.google.android.material.textfield.TextInputLayout pincodeTextView;
    private com.google.android.material.button.MaterialButton dateButton;
    private com.google.android.material.textfield.TextInputLayout balanceTextView;
    private Giftcard giftCardToBeAdded;
    private Date date;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();




    private FloatingActionButton confirmationFab;
    private GiftCardListFragment.Callbacks callback;


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
        balanceTextView = giftCardFragmentLayout.findViewById(R.id.enterBalance);
        dateButton = giftCardFragmentLayout.findViewById(R.id.enterExpirationDate);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(NewGiftCardFragment.this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

                Log.i(TAG, "clicked");
            }
        });




        confirmationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmUserInput()) {
                    callback.finishedEditCard();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            GiftCardListFragment.giftCardDatabase.giftCardDao().addNewGiftCard(giftCardToBeAdded);

                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                }
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
        callback = (GiftCardListFragment.Callbacks) context;
    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datepicker");
    }


    @Override
    public void finishedPickingDate(Date userSelectedDate) {
        date = userSelectedDate;
        dateButton.setText(date.toString());
    }

    private boolean confirmUserInput() {
        if (merchantTextView.getEditText().getText() == null ||
        barcodeTextView.getEditText().getText() == null ||
        pincodeTextView.getEditText().getText() == null ||
        balanceTextView.getEditText().getText() == null ||
        date == null) {
            return false;
        } else {
            float f = Float.parseFloat(balanceTextView.getEditText().getText().toString());
            Integer i = Integer.parseInt(pincodeTextView.getEditText().getText().toString());
            giftCardToBeAdded = new Giftcard(merchantTextView.getEditText().getText().toString(), f, date, barcodeTextView.getEditText().getText().toString(),
                    i);
            return true;
        }

    }
}
