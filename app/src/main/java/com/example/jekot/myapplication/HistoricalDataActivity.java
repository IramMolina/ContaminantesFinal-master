package com.example.jekot.myapplication;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HistoricalDataActivity extends Fragment {

    private LineChart lineChart;

    private LineDataSet lineDataSetNO2;
    private LineDataSet lineDataSetPM10;
    private LineDataSet lineDataSetPM25;

    private List<ILineDataSet> dataSets;

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

        Spinner selectorFiltros = (Spinner) getActivity().findViewById(R.id.selector_contaminante);
        selectorFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){
                    case 0:
                        lineChart.setData(new LineData(dataSets));
                        break;
                    case 1:
                        lineChart.setData(new LineData(lineDataSetNO2));
                        break;
                    case 2:
                        lineChart.setData(new LineData(lineDataSetPM10));
                        break;
                    case 3:
                        lineChart.setData(new LineData(lineDataSetPM25));
                        break;
                    default:
                        lineChart.setData(new LineData(dataSets));
                        break;
                }

                lineChart.invalidate();
                System.gc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void initializeLineChart(){

        lineChart = (LineChart) this.getView().findViewById(R.id.line_chart_historical);
        lineChart.setDescription("");

        for(int i=0; i<getData1().size(); i++)

        lineChart.setDrawBorders(true);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getAxisLeft().setDrawAxisLine(true);

        lineDataSetNO2 = new LineDataSet(getData1(),"NO2");
        lineDataSetPM10 = new LineDataSet(getData2(),"PM10");
        lineDataSetPM25 = new LineDataSet(getData3(), "PM25");


        lineDataSetNO2.setColor(Color.parseColor("#34A0FF"));
        lineDataSetPM10.setColor(Color.parseColor("#1FE8DF"));
        lineDataSetPM25.setColor(Color.parseColor("#C1E82F"));

        lineDataSetNO2.setCircleColor(Color.parseColor("#34A0FF"));
        lineDataSetPM10.setCircleColor(Color.parseColor("#1FE8DF"));
        lineDataSetPM25.setCircleColor(Color.parseColor("#C1E82F"));

        dataSets = new ArrayList<>();
        dataSets.add(lineDataSetNO2);
        dataSets.add(lineDataSetPM10);
        dataSets.add(lineDataSetPM25);

        LineData lineData = new LineData(dataSets);


        lineChart.setData(lineData);


        lineChart.invalidate();
    }

    protected ArrayList<Entry> getData1(){
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

    protected ArrayList<Entry> getData2(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 2f));
        entries.add(new Entry(1f, 4f));
        entries.add(new Entry(2f, 6f));
        entries.add(new Entry(3f, 8f));
        entries.add(new Entry(4f, 9f));
        entries.add(new Entry(5f, 10f));
        entries.add(new Entry(6f, 10.5f));


        return entries;
    }


    protected ArrayList<Entry> getData3(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 10f));
        entries.add(new Entry(1f, 9f));
        entries.add(new Entry(2f, 8f));
        entries.add(new Entry(3f, 7f));
        entries.add(new Entry(4f, 6f));
        entries.add(new Entry(5f, 5f));
        entries.add(new Entry(6f, 4f));


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
