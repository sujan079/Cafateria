package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;

public class RoutineDaysAdapter extends RecyclerView.Adapter<RoutineDaysAdapter.DaysViewHolder>{

    private String[] days={"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    private OnRotineDaysClickListner rotineDaysClickListner;

    public void setRotineDaysClickListner(OnRotineDaysClickListner rotineDaysClickListner) {
        this.rotineDaysClickListner = rotineDaysClickListner;
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_days_list,parent,false);
        return new DaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
        holder.bind(days[position]);
    }

    @Override
    public int getItemCount() {
        return days.length;
    }


    class DaysViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvDay;
        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvDay=itemView.findViewById(R.id.tv_day);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotineDaysClickListner.daysItemClick(days[getAdapterPosition()]);
                }
            });
        }

        public void bind(String day){
            mTvDay.setText(day);
        }
    }

    public interface OnRotineDaysClickListner{
        void daysItemClick(String day);
    }
}
