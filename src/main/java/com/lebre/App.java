package com.lebre;

import java.util.*;
import java.io.*;

public class App {

    public static void main(String[] args) {

        double max, newDim, actualDim, totalConsumption, consumption, lastMessageTime, firstTime, i;
        newDim = actualDim = totalConsumption = consumption = i = lastMessageTime = firstTime = 0;
        max = 5;

        try {

            System.out.println(new File(".").getAbsolutePath());
            Scanner scanner = new Scanner(new File("./src/test/java/com/lebre/test1"));

            while (scanner.hasNextLine()) {

                String val = scanner.nextLine();
                
                if(val.matches("EOF")) break;


                System.out.println("LINE: " + val);
                String[] parts = val.split(" ");

                if ((i == 0)&&(parts.length < 2)) {
                    firstTime = Double.valueOf(parts[0]);
                    consumption = 0;
                } else {
                    if(lastMessageTime!=Double.valueOf(parts[0]))
                    {
                    consumption = secToHours(Double.valueOf(parts[0])-lastMessageTime)*actualDim*max;
                    }else{
                        System.out.println("LINE DISCARDED because of repetitive timestamps." );
                        val = "";
                    }
                }
                
                totalConsumption += consumption;
                
                if (val.matches(".*\\bDelta\\b.*")) {
                    newDim = Double.valueOf(parts[2]);
                    actualDim += newDim;
                    lastMessageTime = Integer.valueOf(parts[0]);
                    System.out.println("LAST T: " + lastMessageTime + " CURRENT T: " + parts[0]);
                    System.out.println("Dimmed by: " + newDim + " to: " + actualDim);
                    System.out.println("Wh increased by: " + consumption + " to total: " + totalConsumption);
                }

                if (val.matches(".*\\bTurnOff\\b.*")) {
                    actualDim = 0;
                    System.out.println("Turned Off");
                    System.out.println("Wh increased by: " + consumption + " to total: " + totalConsumption);
                }
                i++;
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Estimated energy used: " + totalConsumption + " Wh");
    }

    public static double secToHours(double secs) {
        System.out.println("Seconds passed: " + (secs / ( 60 * 60)));
        return (secs / ( 60 * 60));
    }
}
