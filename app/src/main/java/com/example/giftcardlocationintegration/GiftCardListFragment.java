package com.example.giftcardlocationintegration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.giftcardlocationintegration.database.GiftCardDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftCardListFragment extends Fragment {

    private GiftCardAdapter giftCardAdapter;
    private RecyclerView giftCardRecyclerView;
    private FloatingActionButton newItemFab;
    private Callbacks callback;
    public static GiftCardDatabase giftCardDatabase;
    private String dataBaseName = "giftcardsdatabase";

    public interface Callbacks {
         void addNewCardClicked();
         void finishedEditCard();
         //void finishedPickingDate(Date date);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftCardDatabase = Room.databaseBuilder(getContext(), GiftCardDatabase.class, dataBaseName).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View giftCardFragmentLayout = inflater.inflate(R.layout.fragment_usercards, container, false);

        giftCardRecyclerView = giftCardFragmentLayout.findViewById(R.id.userCardsRecyclerView);
        newItemFab = giftCardFragmentLayout.findViewById(R.id.confirmationFab);
        giftCardAdapter = new GiftCardAdapter(generateTestingList(), getLayoutInflater());


        giftCardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        giftCardRecyclerView.setAdapter(giftCardAdapter);


        newItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addNewCardClicked();
            }
        });

        return giftCardFragmentLayout;

    }

    public static GiftCardListFragment newInstance() {
        return new GiftCardListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (Callbacks) context;
    }

    //Temporary method created to generate a testing list, will later be replaced with actual user data
    private List<Giftcard> generateTestingList() {
        List<Giftcard> testList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            testList.add(new Giftcard("Name: " + i, 1.002f, new Date(), "i", i));
        }
        return testList;
    }
}
