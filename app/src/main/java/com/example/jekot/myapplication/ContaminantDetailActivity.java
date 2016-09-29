package com.example.jekot.myapplication;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContaminantDetailActivity extends Fragment {

    private long percentage = 0;
    private String compoundName;
    private double ppm = 0.0;
    private String description = "Esta es una descripción del contaminante o recomendaciones y cuidados ante un exceso del mismo.";
    private int circleColor = R.color.colorIMECA_Regular;
    // http://pollutiondrone.com/android.php

    public ContaminantDetailActivity(){
        super();
    }

    public ContaminantDetailActivity(String compoundName,long percentage ,double ppm, String description, int circleColor){
        super();

        this.percentage = percentage;
        this.compoundName = compoundName;
        this.ppm = ppm;
        this.description = description;
        this.circleColor = circleColor;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contaminant_detail, container, false);
        super.onCreateView(inflater, container,savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView circleTextDetail = (TextView) getView().findViewById(R.id.circle_text_detail);
        TextView compoundNameText = (TextView) getView().findViewById(R.id.compound_name);
        TextView compoundPPMAmountText = (TextView) getView().findViewById(R.id.compound_ppm_amount);
        TextView compoundDescriptionText = (TextView) getView().findViewById(R.id.compound_description_text);

        // Asignamos los valores de porcentajes y descripciones pasados en el constructor
        circleTextDetail.setText(String.valueOf(this.percentage));
        compoundNameText.setText(this.compoundName);
        compoundPPMAmountText.setText(String.format("%1$,.3f", this.ppm) + " ppm");
        compoundDescriptionText.setText(this.description);


        GradientDrawable circleDetail_BG = (GradientDrawable) ((TextView) getActivity().findViewById(R.id.circle_text_detail)).getBackground();
        circleDetail_BG.setColor(ContextCompat.getColor(getContext(),circleColor));


        ((Button) getActivity().findViewById(R.id.go_back_detail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new MainScreenActivity();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_navigation_drawer,frag)
                        .commit();
            }
        });


    }

}
