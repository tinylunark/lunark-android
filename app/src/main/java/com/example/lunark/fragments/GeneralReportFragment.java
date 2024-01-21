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
import com.example.lunark.models.DailyReport;
import com.example.lunark.repositories.ReportRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.snackbar.Snackbar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GeneralReportFragment extends Fragment {
    private static final String TAG = "GeneralReportFragment";
    FragmentGeneralReportBinding mBinding;
    @Inject
    ReportRepository mReportRepository;
    LineChart mChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGeneralReportBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = mBinding.chart;
        mChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                LocalDate date = LocalDate.ofEpochDay((long) value);
                return date.toString();
            }
        });

        mBinding.btnGetReports.setOnClickListener(v -> {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd").toFormatter();

                String startDate = LocalDate.parse(mBinding.etStartDate.getText().toString(), formatter).toString();
                String endDate = LocalDate.parse(mBinding.etEndDate.getText().toString(), formatter).toString();

                mReportRepository.getGeneralReport(startDate, endDate).observe(getViewLifecycleOwner(), generalReport -> {
                    Log.d(TAG, "Daily reports size: " + generalReport.getDailyReports().size());
                    mBinding.totalProfitValue.setText(generalReport.getTotalProfit().toString());
                    mBinding.totalReservationCountValue.setText(generalReport.getTotalReservationCount().toString());
                    setUpChart(new ArrayList<>(generalReport.getDailyReports()));
                });
            } catch (DateTimeParseException e) {
                Snackbar.make(mBinding.getRoot(), R.string.invalid_date_format, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpChart(List<DailyReport> dailyReports) {
        List<Entry> profitEntries = new ArrayList<>();
        List<Entry> reservationCountEntries = new ArrayList<>();
        for (DailyReport dailyReport : dailyReports) {
            profitEntries.add(new Entry(dailyReport.getDate().toEpochDay(), dailyReport.getProfit().floatValue()));
            reservationCountEntries.add(new Entry(dailyReport.getDate().toEpochDay(), dailyReport.getReservationCount().floatValue()));
        }

        LineDataSet profitDataSet = new LineDataSet(profitEntries, "Profit");
        profitDataSet.setColor(R.color.md_theme_dark_error);
        LineDataSet reservationCountDataSet = new LineDataSet(reservationCountEntries, "Reservation count");

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(profitDataSet);
        dataSets.add(reservationCountDataSet);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }
}