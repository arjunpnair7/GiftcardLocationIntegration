package com.example.giftcardlocationintegration;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
         void editExistingCard(String cardName, float cardBalance, Date cardExpiration, String cardBarcodeNumber, int cardPinCode);


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        giftCardDatabase = Room.databaseBuilder(getContext(), GiftCardDatabase.class, dataBaseName).build();
        ViewModelProvider provider = new ViewModelProvider(GiftCardListFragment.this);
        giftCardViewModel = provider.get(GiftCardViewModel.class);
        createNotificationChannel();
        Log.i(TAG, "create");




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
        Log.i(TAG, "created menu");

        sw.setChecked(GiftCardViewModel.useLocationServices);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                permissionCheck();

                GiftCardViewModel.useLocationServices = isChecked;
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getActivity().startForegroundService(new Intent(getContext(), GiftCardLocationService.class));
                    } else {
                        getActivity().startService(new Intent(getContext(), GiftCardLocationService.class));
                    }

                } else {
                    getActivity().stopService(new Intent(getContext(), GiftCardLocationService.class));
                }
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                giftCardAdapter = new GiftCardAdapter(giftcards, getLayoutInflater(), getActivity());
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

    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Needed permissions")
                    .setMessage("This app needs to access location in order to notify you of nearby giftcard locations")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "granted");
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Location services")
                    .setMessage("Enabling location services will allow this app to notify you when you are near a store that matchers a giftcard you have " +
                            "registered. If you do not accept, you must later go into your app's settings and manually enable location services.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationPermissions();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
