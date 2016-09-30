package com.example.jekot.myapplication;

/**
 * Created by a_ira on 28/09/2016.
 */

public class IMECACalculator {
    // Este arreglo representa en orden descendiente los valores para calidad del aire según el IMECA.
    private static String[] IMECA_VALUES = {"Buena", "Regular", "Mala", "Muy Mala", "Extremadamente Mala"};

    // Obtiene el texto IMECA de el contaminante especificado por contaminant y el número de value.
    public static String getIMECAEstimate(String contaminant, double value){
        if(contaminant.equals("NO2")){return getRangeFor(calculateNO2(value));}
        else if(contaminant.equals("PM10")){return getRangeFor(calculatePM10(value));}
        else if(contaminant.equals("PM25")){return  getRangeFor(calculatePM25(value));}
        return "ERROR";
    }

    /*  Recordar que para cada contaminante, su cálculo para obtener el valor de IMECA es distinto */
    // Partículas menores a 10 ug
    public static double calculatePM10(double pM10){
        if(pM10 <= 120){
            return (pM10 *5)/6 ;
        }
        else if(pM10 >= 121 && pM10 <= 320){
            return 40 + (pM10 * 0.5);
        }
        else{
            return (pM10 * 5)/8 ;
        }
    }

    // NO2
    public static double calculateNO2(double nO2){
        if(nO2 <= 0.105){
            return (nO2 *50)/ 0.105;
        }
        else if(nO2 >= 0.106 && nO2 <= 0.210){
            return 1.058 + ((nO2 *49)/0.104);
        }
        else if(nO2 >= 0.211 && nO2 <= 0.315){
            return 1.587 + ((nO2 *49)/0.104);
        }
        else if(nO2 >= 0.316 && nO2 <= 0.420){
            return 2.115 + ((nO2 *49)/0.104);
        }
        else{
            return (nO2 * 201)/0.421 ;
        }
    }

    // Partícuas menores a 2.5 ug
    public static double calculatePM25(double pM25){
        if(pM25 <= 15.4){
            return (pM25*50) / 15.4;
        }
        else if(pM25 >= 15.5 && pM25 <= 40.4){
            return 20.50 + ((pM25 *49)/24.9);
        }
        else if(pM25 >= 40.5 && pM25 <= 65.4){
            return 21.30 + ((pM25 *49)/24.9);
        }
        else if(pM25 >= 65.5 && pM25 <= 150.4){
            return 113.20 + ((pM25 *49)/84.9);
        }
        else{
            return (pM25*201) /150.5;
        }
    }

    // De acuerdo al valor IMECA, regresamos su descripción de índice de calidad.
    public static String getRangeFor(double calculationValue){
        if(calculationValue <= 50.0){
            return IMECA_VALUES[0];
        }
        else if (calculationValue > 50.0 && calculationValue <= 100){
            return IMECA_VALUES[1];
        }
        else if (calculationValue > 100.0 && calculationValue <= 150){
            return IMECA_VALUES[2];
        }
        else if (calculationValue > 150.0 && calculationValue <= 200){
            return IMECA_VALUES[3];
        }
        return IMECA_VALUES[4];
    }

    // Obtiene el color de la librería de colores según su descripción del IMECA
    public static int getColor(String IMECAValue){
        switch(IMECAValue){
            case "Buena":
                return R.color.colorIMECA_Bueno;
            case "Regular":
                return R.color.colorIMECA_Regular;
            case "Mala":
                return R.color.colorIMECA_Mala;
            case "Muy Mala":
                return R.color.colorIMECA_MuyMala;
            case "Extremadamente Mala":
                return R.color.colorIMECA_ExtremadamenteMala;
            default:
                return R.color.colorPrimary;
        }
    }

    // Obtiene la imagen de la librería de colores según su descripción del IMECA
    public static int getImage(String IMECAValue){
        switch(IMECAValue){
            case "Buena":
                return R.mipmap.recomendaciones_buena;
            case "Regular":
                return R.mipmap.recomendaciones_regular;
            case "Mala":
                return R.mipmap.recomendaciones_mala;
            case "Muy Mala":
                return R.mipmap.recomendaciones_muymala;
            case "Extremadamente Mala":
                return R.mipmap.recomendaciones_extremadamentemala;
            default:
                return R.mipmap.ic_launcher;
        }
    }
}
