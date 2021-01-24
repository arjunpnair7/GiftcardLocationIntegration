package com.example.giftcardlocationintegration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardViewHolder> {

    private List<Giftcard> giftCardList;
    private LayoutInflater layoutInflater;

    public GiftCardAdapter(List<Giftcard> giftCardList, LayoutInflater layoutInflater) {
        this.giftCardList = giftCardList;
        this.layoutInflater = layoutInflater;
    }


    @NonNull
    @Override
    public GiftCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View giftCardViewHolderView = layoutInflater.inflate(R.layout.fragment_usercarditem, parent, false);
        return new GiftCardViewHolder(giftCardViewHolderView);
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
