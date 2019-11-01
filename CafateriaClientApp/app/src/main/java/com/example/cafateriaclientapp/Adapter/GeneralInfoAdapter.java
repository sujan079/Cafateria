package com.example.cafateriaclientapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.Network.GSON_Models.User.User;
import com.example.cafateriaclientapp.R;

import java.util.HashMap;
import java.util.List;

public class GeneralInfoAdapter extends RecyclerView.Adapter<GeneralInfoAdapter.ViewHolder> {

    private List<String> infoList;

    public GeneralInfoAdapter(List<String> infoList) {
        this.infoList = infoList;
    }

    public void setInfoList(List<String> infoList) {
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.general_info_list_sample,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bindData(infoList.get(position));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvInfoValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfoValue=itemView.findViewById(R.id.tv_info_value);
        }

        public void bindData(String value){
            tvInfoValue.setText(value);
        }
    }
}
