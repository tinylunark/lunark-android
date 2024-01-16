package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentGeneralReportBinding;
import com.example.lunark.repositories.ReportRepository;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GeneralReportFragment extends Fragment {
    private static final String TAG = "GeneralReportFragment";
    FragmentGeneralReportBinding mBinding;
    @Inject
    ReportRepository mReportRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

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

        mReportRepository.getGeneralReport("2020-01-01", "2025-01-01").observe(getViewLifecycleOwner(), generalReport -> {
            Log.d(TAG, "Daily reports size: " + generalReport.getDailyReports().size());
        });
    }
}