package com.example.tabnewlayout;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Person> items = new ArrayList<>();
    private  SharedViewModel sharedViewModel;

    public ItemAdapter(List<Person> items, SharedViewModel sharedViewModel) {
        if (items != null) {
            this.items = new ArrayList<>(items);
            this.sharedViewModel=sharedViewModel;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = items.get(position);
        holder.textname.setText(person.getName()); // Assuming `Person` has `getName()`
        holder.textage.setText(person.getAge());

        // Decode the encrypted image before displaying it
        String base64Image = person.getBase64Image();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap decodedBitmap = ImageEncryption.decodeBase64ToBitmap(base64Image);
                holder.imageView.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imageView.setImageResource(R.drawable.image_search); // If error occurs, show placeholder image
            }
        } else {
            holder.imageView.setImageResource(R.drawable.image_search); // Show placeholder if no image
        }
        holder.delimg.setOnClickListener(v -> sharedViewModel.deletePerson(person));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Person getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<Person> updatedItems) {
        if (updatedItems == null) return;

        List<Person> newList = new ArrayList<>(updatedItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(items, newList));
        items.clear();
        items.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textname;
        TextView textage;
        ImageView delimg;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textname = itemView.findViewById(R.id.itemText1);
            textage=itemView.findViewById(R.id.itemText2);
            delimg=itemView.findViewById(R.id.delete);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class DiffCallback extends DiffUtil.Callback {
        private final List<Person> oldList;
        private final List<Person> newList;

        public DiffCallback(List<Person> oldList, List<Person> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}