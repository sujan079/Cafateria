package com.example.cafateriaadminapp.ui.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Adapter.RoutineDaysAdapter;
import com.example.cafateriaadminapp.R;
import com.example.cafateriaadminapp.RoutineItemActivity;

public class RoutineFragment extends Fragment {


    private RecyclerView rvDays;
    private RoutineDaysAdapter routineDaysAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routine, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvDays=view.findViewById(R.id.rv_routine_days);
        routineDaysAdapter=new RoutineDaysAdapter();
        layoutManager=new GridLayoutManager(getContext(),2);

        rvDays.setAdapter(routineDaysAdapter);
        rvDays.setLayoutManager(layoutManager);
        rvDays.setHasFixedSize(true);

        routineDaysAdapter.setRotineDaysClickListner(new RoutineDaysAdapter.OnRotineDaysClickListner() {
            @Override
            public void daysItemClick(String day) {
                Intent intent=new Intent(getContext(), RoutineItemActivity.class);
                intent.putExtra(RoutineItemActivity.EXTRA_DAY,day);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
