package com.example.giftcardlocationintegration;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.temporal.TemporalAccessor;
import java.util.List;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardViewHolder> {

    private List<Giftcard> giftCardList;
    private LayoutInflater layoutInflater;
    private String TAG = "GiftCardAdapter";
    private Activity myActivity;

    public GiftCardAdapter(List<Giftcard> giftCardList, LayoutInflater layoutInflater, Activity myActivity) {
        this.giftCardList = giftCardList;
        this.layoutInflater = layoutInflater;
        this.myActivity = myActivity;
    }


    @NonNull
    @Override
    public GiftCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View giftCardViewHolderView = layoutInflater.inflate(R.layout.fragment_usercarditem, parent, false);
        giftCardViewHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked on a giftcard");
            }
        });
        return new GiftCardViewHolder(giftCardViewHolderView, myActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftCardViewHolder holder, int position) {
        holder.updateGiftCardViewHolder(giftCardList.get(position));
    }

    @Override
    public int getItemCount() {
        return giftCardList.size();
    }
}
