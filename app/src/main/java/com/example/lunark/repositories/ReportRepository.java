package com.example.lunark.repositories;

import androidx.lifecycle.LiveData;

import com.example.lunark.datasources.ReportNetworkDataSource;
import com.example.lunark.models.GeneralReport;
import com.example.lunark.models.PropertyReport;

import javax.inject.Inject;

public class ReportRepository {
    public ReportNetworkDataSource mReportNetworkDataSource;

    @Inject
    public ReportRepository(ReportNetworkDataSource reportNetworkDataSource) {
        mReportNetworkDataSource = reportNetworkDataSource;
    }

    public LiveData<GeneralReport> getGeneralReport(String start, String end) {
        return mReportNetworkDataSource.getGeneralReport(start, end);
    }

    public LiveData<PropertyReport> getPropertyReport(String year, String propertyId) {
        return mReportNetworkDataSource.getPropertyReport(year, propertyId);
    }
}
