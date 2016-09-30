package com.example.jekot.myapplication;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends Fragment {

    private double IMECATotalValue = 49;
    private String IMECATotalText = "Regular";
    private double[] IMECAContaminantValue = {0.110,40,200}; // {no2,pm10,pm25}
    private String[] IMECADescripciones = {"Su alta concentracion puede provocar disminución de la función pulmonar y aumentar el riesgo de aparición de síntomas respiratorios como bronquitis aguda, tos y flemas, especialmente en los niños.",
                                            "Las altas concentraciones de éste compuesto permite que el material particulado penetre por la nariz y la garganta, llegue a los pulmones y provoque problemas de respiración e irritación de los capilares pulmonares.",
                                            "La exposisión prolongada a éstos compuestos causa bronquitis y dolencias cardiovasculares."};
    private String timestampText = "Cargando";
    private String temperatureText = "40 °C";
    private String altitudeText = "4000 m";

    public static boolean stillOnScreen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_screen, container, false);
        super.onCreateView(inflater, container,savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stillOnScreen = true;
        HistoricalDataActivity.stillOnScreen = false;

        getCurrentData();
        /*
        initializeCircleChart();
        initializeButtons();
        initializeValues();*/
    }

    private void initializeValues() {
        this.IMECATotalValue = IMECACalculator.calculatePM10(IMECAContaminantValue[1]);
        this.IMECATotalText = IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1]);

        TextView calidadAireTotal_View = (TextView) getActivity().findViewById(R.id.calidad_aire_general_main);
        calidadAireTotal_View.setText(IMECATotalText);
        calidadAireTotal_View.setBackgroundResource(IMECACalculator.getColor(IMECATotalText));

        TextView IMECATotalValue_view = (TextView) getActivity().findViewById(R.id.imecas_total_numero);
        IMECATotalValue_view.setText(String.valueOf(Math.round(IMECATotalValue)));

        TextView IMECATimestampMostRecent_view = (TextView) getActivity().findViewById(R.id.timestamp_texto);
        IMECATimestampMostRecent_view.setText("Última actualización: ".concat(timestampText));


        GradientDrawable gd_NO2 = (GradientDrawable) ((TextView) getActivity().findViewById(R.id.no2_circle_text)).getBackground();
        gd_NO2.setColor(ContextCompat.getColor(getContext(),IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("NO2",IMECAContaminantValue[0]))));

        GradientDrawable gd_PM10 = (GradientDrawable) ((TextView) getActivity().findViewById(R.id.pm10_circle_text)).getBackground();
        gd_PM10.setColor(ContextCompat.getColor(getContext(),IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1]))));

        GradientDrawable gd_PM25 = (GradientDrawable) ((TextView) getActivity().findViewById(R.id.pm2_5_circle_text)).getBackground();
        gd_PM25.setColor(ContextCompat.getColor(getContext(),IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM25",IMECAContaminantValue[2]))));

        ImageView recommendationsImageView = (ImageView) getActivity().findViewById(R.id.recommendations_imageview);
        recommendationsImageView.setImageResource(IMECACalculator.getImage(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1])));


        //ImageView imecaRecommendations = (ImageView) getActivity().findViewById(R.id.imagen_imecas_explicacion);
        //imecaRecommendations.setImageResource(R.mipmap.imecas_explicacion);
    }


    public void initializeButtons(){

        // Para NOx
        TextView textViewNO2 = (TextView) getView().findViewById(R.id.no2_circle_text);
        TextView textViewPM10 = (TextView) getView().findViewById(R.id.pm10_circle_text);
        TextView textViewPM25 = (TextView) getView().findViewById(R.id.pm2_5_circle_text);

        textViewNO2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fragment frag = new ContaminantDetailActivity("Óxido de Nitrógeno",Math.round(IMECACalculator.calculateNO2(IMECAContaminantValue[0])), IMECAContaminantValue[0], IMECADescripciones[0],IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("NO2",IMECAContaminantValue[0])));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_navigation_drawer,frag)
                        .commit();
                return true;
            }
        });

        textViewPM10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fragment frag = new ContaminantDetailActivity("Partículas menores a 10 micrómetros",Math.round(IMECACalculator.calculatePM10(IMECAContaminantValue[1])), IMECAContaminantValue[1], IMECADescripciones[1],IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1])));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_navigation_drawer,frag)
                        .commit();
                return true;
            }
        });

        textViewPM25.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fragment frag = new ContaminantDetailActivity("Partículas menores a 2.5 micrómetros",Math.round(IMECACalculator.calculatePM25(IMECAContaminantValue[2])), IMECAContaminantValue[2], IMECADescripciones[2], IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM25",IMECAContaminantValue[2])));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_navigation_drawer,frag)
                        .commit();
                return true;
            }
        });


    }


    public void initializeCircleChart(){
        final PieChart pieChart = (PieChart) this.getView().findViewById(R.id.pie_chart_current);

        final PieDataSet pieDataSet = new PieDataSet(getData(), "Porcentajes de Contaminantes");
        pieDataSet.setColors(new int[]{IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("NO2",IMECAContaminantValue[0])),IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1])),IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM25",IMECAContaminantValue[2]))},getContext());
        pieDataSet.setDrawValues(false);



        PieData pieData = new PieData(pieDataSet);

        pieChart.setDescription("");
        pieDataSet.setValueTextSize(25f);


        pieChart.setData(pieData);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int switchValue = Math.round(h.getX());
                TextView calidadContaminanteText_view = (TextView) getActivity().findViewById(R.id.calidad_aire_contaminante_main);
                int color = R.color.colorPrimaryDark;

                switch (switchValue){
                    case 0:
                        System.out.println("NO2");
                        calidadContaminanteText_view.setText(IMECACalculator.getIMECAEstimate("NO2",IMECAContaminantValue[0]));
                        color = IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("NO2",IMECAContaminantValue[0]));
                        break;
                    case 1:
                        System.out.println("PM10");
                        calidadContaminanteText_view.setText(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1]));
                        color = IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM10",IMECAContaminantValue[1]));
                        break;
                    case 2:
                        System.out.println("PM25");
                        calidadContaminanteText_view.setText(IMECACalculator.getIMECAEstimate("PM25",IMECAContaminantValue[2]));
                        color = IMECACalculator.getColor(IMECACalculator.getIMECAEstimate("PM25",IMECAContaminantValue[2]));
                        break;
                    default:
                        System.out.println("Error");
                        break;
                }


                calidadContaminanteText_view.setBackgroundResource(color);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.highlightValue(1,0,true);

        pieChart.invalidate();
    }


    public List<PieEntry> getData(){
        List<PieEntry> arr = new ArrayList<>();
        arr.add(0, new PieEntry(33.4f, getResources().getString(R.string.NO2)));
        arr.add(1, new PieEntry(33.3f, getResources().getString(R.string.PM10)));
        arr.add(2, new PieEntry(33.3f, getResources().getString(R.string.PM25)));

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
                                JSONObject mostRecentData = (JSONObject) products.get(products.length() - 1);


                                IMECAContaminantValue[0] = Double.valueOf((String) mostRecentData.get("no2"));
                                IMECAContaminantValue[1] = Double.valueOf((String) mostRecentData.get("pm10"));
                                IMECAContaminantValue[2] = Double.valueOf((String) mostRecentData.get("pm25"));

                                timestampText = (String) mostRecentData.get("timestamp");


                                if(stillOnScreen){initializeCircleChart();}
                                if(stillOnScreen){initializeButtons();}
                                if(stillOnScreen){initializeValues();}

                                TextView temperatureTextView = (TextView) getActivity().findViewById(R.id.sidebar_temperature);
                                String temperatureString = "Temperatura: " + mostRecentData.getString("temperature") + " °C";
                                temperatureTextView.setText(temperatureString);;

                                TextView altitudeTextView = (TextView) getActivity().findViewById(R.id.sidebar_altitud);
                                String altitudeString = "Altitud: "+mostRecentData.getString("temperature") + " m";
                                altitudeTextView.setText(altitudeString);


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
