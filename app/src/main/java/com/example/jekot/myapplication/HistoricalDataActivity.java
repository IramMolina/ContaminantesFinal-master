package com.example.jekot.myapplication;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HistoricalDataActivity extends Fragment {

    private LineChart lineChart;

    private LineDataSet lineDataSetNO2;
    private LineDataSet lineDataSetPM10;
    private LineDataSet lineDataSetPM25;


    private List<ILineDataSet> dataSets;


    private LineData lineDataAll;
    private LineData lineDataNO2;
    private LineData lineDataPM10;
    private LineData lineDataPM25;

    public static boolean stillOnScreen;

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

        stillOnScreen = true;
        MainScreenActivity.stillOnScreen = false;

        getCurrentData();
    }

    protected void initializeLineChart(ArrayList<JSONObject> products){

        lineChart = (LineChart) this.getView().findViewById(R.id.line_chart_historical);
        lineChart.setDescription("");

        ArrayList<Entry> entriesNO2 = new ArrayList<>();
        ArrayList<Entry> entriesPM10 = new ArrayList<>();
        ArrayList<Entry> entriesPM25 = new ArrayList<>();
        String datesSelected = "";

        for(int i = 0; i < products.size(); i++){
            try {
                JSONObject product = (JSONObject) products.get(i);
                datesSelected += (i+1) + ": " + product.getString("timestamp") + "\n";

                entriesNO2.add(new Entry(i,(float)IMECACalculator.calculateNO2(Double.valueOf((String) product.get("no2")))));
                entriesPM10.add(new Entry(i,(float)IMECACalculator.calculatePM10(Double.valueOf((String) product.get("pm10")))));
                entriesPM25.add(new Entry(i,(float)IMECACalculator.calculatePM25(Double.valueOf((String) product.get("pm25")))));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        lineChart.setDrawBorders(true);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getAxisLeft().setDrawAxisLine(true);



        lineDataSetNO2 = new LineDataSet(entriesNO2,"NO2");
        lineDataSetPM10 = new LineDataSet(entriesPM10,"PM10");
        lineDataSetPM25 = new LineDataSet(entriesPM25, "PM25");

        lineChart.getXAxis().setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


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

        lineDataNO2 = new LineData(lineDataSetNO2);
        lineDataPM10 = new LineData(lineDataSetPM10);
        lineDataPM25 = new LineData(lineDataSetPM25);
        lineDataAll = new LineData(dataSets);


        lineChart.setData(lineDataAll);


        TextView cargandoTextView = (TextView) getActivity().findViewById(R.id.texto_cargando_historico);
        cargandoTextView.setText("ÚLTIMAS 5 ACTUALIZACIONES:");

        TextView datesSelectedTextView = (TextView) getActivity().findViewById(R.id.texto_fechas_mostradas);
        datesSelectedTextView.setText(datesSelected);

        lineChart.invalidate();

        Spinner selectorFiltros = (Spinner) getActivity().findViewById(R.id.selector_contaminante);
        selectorFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){
                    case 0:
                        lineChart.setData(lineDataAll);
                        break;
                    case 1:
                        lineChart.setData(lineDataNO2);
                        break;
                    case 2:
                        lineChart.setData(lineDataPM10);
                        break;
                    case 3:
                        lineChart.setData(lineDataPM25);
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

    public ArrayList<Entry> getData(){
        ArrayList<Entry> arr = new ArrayList<>();
        arr.add(new Entry(1f,1f));
        arr.add(new Entry(2f,4f));
        arr.add(new Entry(3f, 9f));
        arr.add(new Entry(4f, 16f));
        arr.add(new Entry(5f, 25f));
        return arr;
    }




    public void getCurrentData(){
        String URL = "http://www.pollutiondrone.com/todo.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if(stillOnScreen) {
                            try {
                                JSONArray products = (new JSONObject(response)).getJSONArray("products");

                                ArrayList<JSONObject> last5JSONObjects = new ArrayList<>();
                                last5JSONObjects.add(products.getJSONObject(products.length() - 5));
                                last5JSONObjects.add(products.getJSONObject(products.length() - 4));
                                last5JSONObjects.add(products.getJSONObject(products.length() - 3));
                                last5JSONObjects.add(products.getJSONObject(products.length() - 2));
                                last5JSONObjects.add(products.getJSONObject(products.length() - 1));

                                if (stillOnScreen) {
                                    initializeLineChart(last5JSONObjects);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
