package com.example.printerserver.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerserver.Database.Models.IpAdders;
import com.example.printerserver.R;

import java.util.ArrayList;
import java.util.List;

public class IpAddersAdapter extends RecyclerView.Adapter<IpAddersAdapter.IpAddersViewHolder> {

    private List<IpAdders> ipAddersList = new ArrayList<>();
    private OnItemClicked onItemClicked;

    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setIpAddersList(List<IpAdders> ipAddersList) {
        this.ipAddersList = ipAddersList;
    }

    @NonNull
    @Override
    public IpAddersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ip_addr_list, parent, false);

        return new IpAddersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IpAddersViewHolder holder, int position) {
        holder.bind(ipAddersList.get(position));
    }

    @Override
    public int getItemCount() {
        return ipAddersList.size();
    }

    public interface OnItemClicked {
        public void delIp(IpAdders ipAdders);
    }

    public class IpAddersViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvIpAddr, mTvStatus;

        public IpAddersViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvIpAddr = itemView.findViewById(R.id.tv_ipAddr);
            mTvStatus = itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked.delIp(ipAddersList.get(getAdapterPosition()));
                }
            });

        }

        public void bind(IpAdders ipAdders) {
            mTvIpAddr.setText(ipAdders.getIpAddr());
            mTvStatus.setText(ipAdders.getStatus());
        }
    }
}
