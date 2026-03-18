package com.ontariotechu.sofe3980U;

import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Multiclass Classification
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String filePath="model.csv";
        FileReader filereader;
        List<String[]> allData;
        try{
            filereader = new FileReader(filePath); 
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
            allData = csvReader.readAll();
        }
        catch(Exception e){
            System.out.println( "Error reading the CSV file" );
            return;
        }
        
        float ce = 0;
        float epsilon = 0.000001f;
        int[][] confusionMatrix = new int[5][5];
        float[] y_predicted = new float[5];

        for (String[] row : allData) { 
            int y_true = Integer.parseInt(row[0]);

            int predictedClass = 1;
            float maxValue = -1;

            for(int i=0; i<5; i++){
                y_predicted[i] = Float.parseFloat(row[i+1]);

                if(y_predicted[i] > maxValue){
                    maxValue = y_predicted[i];
                    predictedClass = i + 1;
                }
            }

            float trueClassProbability = y_predicted[y_true - 1];
            if(trueClassProbability < epsilon){
                trueClassProbability = epsilon;
            }

            ce += -Math.log(trueClassProbability);

            confusionMatrix[predictedClass - 1][y_true - 1]++;
        }

        ce = ce / allData.size();

        System.out.println("CE =" + ce);
        System.out.println("Confusion matrix");
        System.out.println("\t\ty=1\t\ty=2\t\ty=3\t\ty=4\t\ty=5");

        for(int i=0; i<5; i++){
            System.out.print("\ty^=" + (i+1));
            for(int j=0; j<5; j++){
                System.out.print("\t" + confusionMatrix[i][j]);
            }
            System.out.println();
        }
    }
}