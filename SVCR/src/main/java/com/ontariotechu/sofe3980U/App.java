package com.ontariotechu.sofe3980U;

import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        float[] model1 = evaluateModel("model_1.csv");
        float[] model2 = evaluateModel("model_2.csv");
        float[] model3 = evaluateModel("model_3.csv");

        if(model1[0] < model2[0] && model1[0] < model3[0])
            System.out.println("According to MSE, The best model is model_1.csv");
        else if(model2[0] < model1[0] && model2[0] < model3[0])
            System.out.println("According to MSE, The best model is model_2.csv");
        else
            System.out.println("According to MSE, The best model is model_3.csv");

        if(model1[1] < model2[1] && model1[1] < model3[1])
            System.out.println("According to MAE, The best model is model_1.csv");
        else if(model2[1] < model1[1] && model2[1] < model3[1])
            System.out.println("According to MAE, The best model is model_2.csv");
        else
            System.out.println("According to MAE, The best model is model_3.csv");

        if(model1[2] < model2[2] && model1[2] < model3[2])
            System.out.println("According to MARE, The best model is model_1.csv");
        else if(model2[2] < model1[2] && model2[2] < model3[2])
            System.out.println("According to MARE, The best model is model_2.csv");
        else
            System.out.println("According to MARE, The best model is model_3.csv");
    }

    public static float[] evaluateModel(String filePath)
    {
        FileReader filereader;
        List<String[]> allData;
        try{
            filereader = new FileReader(filePath); 
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
            allData = csvReader.readAll();
        }
        catch(Exception e){
            System.out.println("Error reading the CSV file");
            return new float[]{0,0,0};
        }
        
        float mse = 0;
        float mae = 0;
        float mare = 0;
        float epsilon = 0.000001f;
        int n = allData.size();

        for (String[] row : allData) { 
            float y_true = Float.parseFloat(row[0]);
            float y_predicted = Float.parseFloat(row[1]);

            mse += (y_true - y_predicted) * (y_true - y_predicted);
            mae += Math.abs(y_true - y_predicted);
            mare += Math.abs(y_true - y_predicted) / (Math.abs(y_true) + epsilon);
        }

        mse = mse / n;
        mae = mae / n;
        mare = mare / n;

        System.out.println("for " + filePath);
        System.out.println("\tMSE =" + mse);
        System.out.println("\tMAE =" + mae);
        System.out.println("\tMARE =" + mare);

        return new float[]{mse, mae, mare};
    }
}