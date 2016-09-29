package com.example.jekot.myapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoricalDataActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historical_data, container, false);
        super.onCreateView(inflater, container,savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLineChart();
    }

    protected void initializeLineChart(){

        LineChart lineChart = (LineChart) this.getView().findViewById(R.id.line_chart_historical);

        LineDataSet lineDataSet = new LineDataSet(getData(),"Algo VS Algo");

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);


        lineChart.invalidate();
    }

    protected ArrayList<Entry> getData(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 0));
        entries.add(new Entry(1f, 1));
        entries.add(new Entry(2f, 2));
        entries.add(new Entry(3f, 3));
        entries.add(new Entry(4f, 4));
        entries.add(new Entry(5f, 5));
        entries.add(new Entry(6f, 7));


        return entries;
    }

    protected ArrayList<String> getLabels(){
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Enero");
        labels.add("Enero");
        labels.add("Ener1");
        labels.add("Enero");
        labels.add("Enero");
        labels.add("Ener3");

        return labels;
    }

}
