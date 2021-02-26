package com.rahulkumaryadav.pdd1;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public  class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{


    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int position) {
        String image =notificationModelList.get(position).getImage();
        String body =notificationModelList.get(position).getBody();
        boolean read =notificationModelList.get(position).isRead();
        viewHolder.setData(image,body,read);
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.textview);
        }
        private void setData(String image,String body,boolean read){
            Glide.with(itemView.getContext()).load(image).into(imageView);
            if (read){
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setAlpha(0.7f);
            }else{
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setAlpha(1f);
            }
            textView.setText(body);
        }
    }
}