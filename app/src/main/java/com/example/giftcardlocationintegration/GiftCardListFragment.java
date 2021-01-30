package com.example.giftcardlocationintegration;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.giftcardlocationintegration.database.GiftCardDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftCardListFragment extends Fragment {

    private static final String TAG = "GiftCardListFragment";
    private GiftCardAdapter giftCardAdapter;
    private RecyclerView giftCardRecyclerView;
    private FloatingActionButton newItemFab;
    private Callbacks callback;
    public static GiftCardDatabase giftCardDatabase;
    private String dataBaseName = "giftcardsdatabase";
    private GiftCardViewModel giftCardViewModel;
    public static SwitchCompat sw;

    public interface Callbacks {
         void addNewCardClicked();
         void finishedEditCard();
         //void finishedPickingDate(Date date);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        giftCardDatabase = Room.databaseBuilder(getContext(), GiftCardDatabase.class, dataBaseName).build();
        ViewModelProvider provider = new ViewModelProvider(GiftCardListFragment.this);
        giftCardViewModel = provider.get(GiftCardViewModel.class);
        createNotificationChannel();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.mainmenu, menu);

        MenuItem itemSwitch = menu.findItem(R.id.my_switch);
        itemSwitch.setActionView(R.layout.switch_layout);
        sw = (SwitchCompat) menu.findItem(R.id.my_switch).getActionView().findViewById(R.id.switchForActionBar);


        sw.setChecked(GiftCardViewModel.useLocationServices);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GiftCardViewModel.useLocationServices = isChecked;
                if (isChecked) {
                    getActivity().startForegroundService(new Intent(getContext(), GiftCardLocationService.class));

                } else {
                    getActivity().stopService(new Intent(getContext(), GiftCardLocationService.class));
                }
            }
        });




    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View giftCardFragmentLayout = inflater.inflate(R.layout.fragment_usercards, container, false);

        giftCardRecyclerView = giftCardFragmentLayout.findViewById(R.id.userCardsRecyclerView);
        newItemFab = giftCardFragmentLayout.findViewById(R.id.confirmationFab);

        giftCardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        giftCardViewModel.myGiftCards.observe(getViewLifecycleOwner(), new Observer<List<Giftcard>>() {
            @Override
            public void onChanged(List<Giftcard> giftcards) {
                giftCardAdapter = new GiftCardAdapter(giftcards, getLayoutInflater());
                giftCardRecyclerView.setAdapter(giftCardAdapter);
            }
        });

        giftCardViewModel.currentGiftCardNames.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                GiftCardLocationService.currentUserGiftCards = strings;
                for (int i = 0; i < GiftCardLocationService.currentUserGiftCards.size(); i++) {
                    Log.i(TAG, GiftCardLocationService.currentUserGiftCards.get(i));
                }
            }
        });





        //giftCardAdapter = new GiftCardAdapter(generateTestingList(), getLayoutInflater());


        //giftCardRecyclerView.setAdapter(giftCardAdapter);




        newItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addNewCardClicked();
            }
        });

        return giftCardFragmentLayout;

    }

    public static GiftCardListFragment newInstance() {
        GiftCardListFragment fragment = new GiftCardListFragment();
        fragment.setHasOptionsMenu(true);
        return fragment;
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    GiftCardLocationService.CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
