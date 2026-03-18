package com.ontariotechu.sofe3980U;

import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Binary Regression
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
            System.out.println("According to BCE, The best model is model_1.csv");
        else if(model2[0] < model1[0] && model2[0] < model3[0])
            System.out.println("According to BCE, The best model is model_2.csv");
        else
            System.out.println("According to BCE, The best model is model_3.csv");

        if(model1[1] > model2[1] && model1[1] > model3[1])
            System.out.println("According to Accuracy, The best model is model_1.csv");
        else if(model2[1] > model1[1] && model2[1] > model3[1])
            System.out.println("According to Accuracy, The best model is model_2.csv");
        else
            System.out.println("According to Accuracy, The best model is model_3.csv");

        if(model1[2] > model2[2] && model1[2] > model3[2])
            System.out.println("According to Precision, The best model is model_1.csv");
        else if(model2[2] > model1[2] && model2[2] > model3[2])
            System.out.println("According to Precision, The best model is model_2.csv");
        else
            System.out.println("According to Precision, The best model is model_3.csv");

        if(model1[3] > model2[3] && model1[3] > model3[3])
            System.out.println("According to Recall, The best model is model_1.csv");
        else if(model2[3] > model1[3] && model2[3] > model3[3])
            System.out.println("According to Recall, The best model is model_2.csv");
        else
            System.out.println("According to Recall, The best model is model_3.csv");

        if(model1[4] > model2[4] && model1[4] > model3[4])
            System.out.println("According to F1 score, The best model is model_1.csv");
        else if(model2[4] > model1[4] && model2[4] > model3[4])
            System.out.println("According to F1 score, The best model is model_2.csv");
        else
            System.out.println("According to F1 score, The best model is model_3.csv");

        if(model1[5] > model2[5] && model1[5] > model3[5])
            System.out.println("According to AUC ROC, The best model is model_1.csv");
        else if(model2[5] > model1[5] && model2[5] > model3[5])
            System.out.println("According to AUC ROC, The best model is model_2.csv");
        else
            System.out.println("According to AUC ROC, The best model is model_3.csv");
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
            return new float[]{0,0,0,0,0,0};
        }

        float bce = 0;
        float epsilon = 0.000001f;

        int TP = 0;
        int TN = 0;
        int FP = 0;
        int FN = 0;

        int n_positive = 0;
        int n_negative = 0;

        for (String[] row : allData) { 
            int y_true = Integer.parseInt(row[0]);
            float y_predicted = Float.parseFloat(row[1]);

            if(y_predicted < epsilon) y_predicted = epsilon;
            if(y_predicted > 1 - epsilon) y_predicted = 1 - epsilon;

            if(y_true == 1)
            {
                bce += Math.log(y_predicted);
                n_positive++;
            }
            else
            {
                bce += Math.log(1 - y_predicted);
                n_negative++;
            }

            int y_binary;
            if(y_predicted >= 0.5)
                y_binary = 1;
            else
                y_binary = 0;

            if(y_true == 1 && y_binary == 1) TP++;
            if(y_true == 0 && y_binary == 0) TN++;
            if(y_true == 0 && y_binary == 1) FP++;
            if(y_true == 1 && y_binary == 0) FN++;
        }

        bce = -bce / allData.size();

        float accuracy = (float)(TP + TN) / (TP + TN + FP + FN);
        float precision = (float)TP / (TP + FP);
        float recall = (float)TP / (TP + FN);
        float f1 = 2 * precision * recall / (precision + recall);

        float[] x = new float[101];
        float[] y = new float[101];

        for(int i=0; i<=100; i++)
        {
            float th = i / 100.0f;
            int TP_ROC = 0;
            int FP_ROC = 0;

            for (String[] row : allData) {
                int y_true = Integer.parseInt(row[0]);
                float y_predicted = Float.parseFloat(row[1]);

                if(y_true == 1 && y_predicted >= th) TP_ROC++;
                if(y_true == 0 && y_predicted >= th) FP_ROC++;
            }

            y[i] = (float)TP_ROC / n_positive;
            x[i] = (float)FP_ROC / n_negative;
        }

        float auc = 0;
        for(int i=1; i<=100; i++)
        {
            auc += (y[i-1] + y[i]) * Math.abs(x[i-1] - x[i]) / 2;
        }

        System.out.println("for " + filePath);
        System.out.println("\tBCE =" + bce);
        System.out.println("\tConfusion matrix");
        System.out.println("\t\t\ty=1\t\ty=0");
        System.out.println("\t\ty^=1\t" + TP + "\t" + FP);
        System.out.println("\t\ty^=0\t" + FN + "\t" + TN);
        System.out.println("\tAccuracy =" + accuracy);
        System.out.println("\tPrecision =" + precision);
        System.out.println("\tRecall =" + recall);
        System.out.println("\tf1 score =" + f1);
        System.out.println("\tauc roc =" + auc);

        return new float[]{bce, accuracy, precision, recall, f1, auc};
    }
}