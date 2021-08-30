package com.lebre;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class App {
    public static void main(String[] args) {
        //declarations
        double max, currentDim, newDim, totalConsumption, consumption, lastMessageTime, thisMessageTime;
        int i;
        ArrayList<String[]> arrayList;
        Scanner scanner;

        //inits
        i=0;
        currentDim = newDim = totalConsumption = consumption = lastMessageTime = thisMessageTime = 0;
        max = 5;
        arrayList = new ArrayList<>();
        scanner = new Scanner(System.in);
       
        try {
            // System.out.println(new File(".").getAbsolutePath());
            while (scanner.hasNextLine()) {
                // entire line
                String val = scanner.nextLine();
                //splits into parts
                String[] parts = val.split(" ");

                //TEXT VALIDATION
                //if it is the second EOF it calculates the energy
                if (val.matches("EOF") && (i != 0)) {
                    break;
                }else if(val.matches("EOF") && (i==0))
                {
                    //do nothing
                }else if (parts.length < 2) {
                    //invalid inputs
                    throw new IOException("Input not valid: " + val);
                } else if (!parts[1].matches("Delta") && !parts[1].matches("TurnOff")) {
                    //invalid inputs
                    System.out.println("Input not valid in " + val);
                    break;
                }
                //ENDS TEXT VALIDATION

                if ((i == 0) && val.matches("EOF")) {
                    //is the first iteration
                    consumption = 0;
                } else {
                    // all other cases
                    
                    //records the time
                    thisMessageTime = Double.valueOf(parts[0]);

                    // in each iteration compares the actual time with the last, and only adds if
                    // the item isn't equal. If it is equal it discards the line as duplicate.
                    if (lastMessageTime != Double.valueOf(parts[0])) {
                        arrayList.add(parts);
                        //System.out.println("ADDED");
                    } else {
                        // duplicated line
                        val = "";
                    }
                    //updates the last time record.
                    lastMessageTime = thisMessageTime;
                }
                i++;
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }

        //It orders the arrayList by the String in the 0 position (time)
        Collections.sort(arrayList, new Comparator<String[]>() {
            public int compare(String[] strings, String[] otherStrings) {
                return strings[0].compareTo(otherStrings[0]);
            }
        });

        i = 0;        
        //The arrayList is now ordered and without duplicates.
        for (String[] row : arrayList) {

            if (row[1].matches(".*\\bDelta\\b.*")) {
                //Type: Delta message

                //gets values of dim and time.
                thisMessageTime = Double.valueOf(row[0]);
                newDim = Double.valueOf(row[2]);

                // a quick dim validation if is out of spec
                if (newDim < -1) {
                    newDim = 0;
                } else if (newDim > 1) {
                    newDim = 0;
                }

                //calculates consumption from last time to this time.
                consumption = secToHours(thisMessageTime - lastMessageTime) * currentDim * max;
                //updates the last time record to this time.
                lastMessageTime = thisMessageTime;

                //updates the dim.
                currentDim += newDim;
                //sums the consumptions.
                totalConsumption += consumption;

                // CAP top and bottom.
                if (currentDim > 1) {
                    currentDim = 1;
                } else if (currentDim < 0) {
                    currentDim = 0;
                }
            }

            if (row[1].matches(".*\\bTurnOff\\b.*")) {
                //Type: TurnOff message

                //gets time of current message.
                thisMessageTime = Double.valueOf(row[0]);

                if (!(i == 0)) {
                    //if it is not the first case calculate the consumption.
                    consumption = secToHours(thisMessageTime - lastMessageTime) * currentDim * max;
                } else {
                    //first time resets consumption.
                    consumption = 0;
                }
                //updates
                lastMessageTime = thisMessageTime;
                //resets dims
                newDim = currentDim = 0;
                //sums consumptions
                totalConsumption += consumption;
            }
            i++;

        }
        //Formats for 5 decimal digits max. Removes the right zeroes.
        DecimalFormat format = new DecimalFormat("0.#####");
        System.out.println("Estimated energy used: " + format.format(totalConsumption) + " Wh");
    }

    public static double secToHours(double secs) {
        // from seconds to hours (milliseconds to second done in the declaration)
        return (secs / (60 * 60));
    }
}
