package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentGeneralReportBinding;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GeneralReportFragment extends Fragment {
    FragmentGeneralReportBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding= FragmentGeneralReportBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 3));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(R.color.md_theme_dark_primary);
        dataSet.setValueTextColor(R.color.md_theme_dark_primaryContainer);

        LineData lineData = new LineData(dataSet);
        mBinding.chart.setData(lineData);
        mBinding.chart.invalidate();
    }
}