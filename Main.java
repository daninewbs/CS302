package ClosestSchools;
/*
* Author: Danielle Newberry
* Implements the closest pair of points recursive algorithm
* on locations of K-12 schools in Vermont obtained from http://geodata.vermont.gov/datasets/vt-school-locations-k-12

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.io.File;


public class Main {


	public static void main(String[] args) throws IOException{

		//Creates an ArrayList containing School objects from the .csv file
		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		ArrayList<School> schoolList = new ArrayList <School>();
		// You may have to adjust the file address in the following line to your computer
		BufferedReader br = new BufferedReader(new FileReader("ClosestSchools/Data/VT_School_Locations__K12(1).csv"));
		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			schoolList.add(new School(temp[4],Double.parseDouble(temp[0]),Double.parseDouble(temp[1])));
		}


		//Preprocess the data to create two sorted arrayLists (one by X-coordinate and one by Y-coordinate):
		ArrayList<School> Xsorted = new ArrayList <School>();
		ArrayList<School> Ysorted = new ArrayList <School>();
		Collections.sort(schoolList, new SortbyX());
		Xsorted.addAll(schoolList);
		Collections.sort(schoolList, new SortbyY());
		Ysorted.addAll(schoolList);

		//Run the Recursive Algorithm
		School[] cp = new School[2];
		cp = ClosestPoints(Xsorted,Ysorted);
		if(cp[0]!=null)
			System.out.println("The two closest schools are "+ cp[0].name + " and " + cp[1].name +".");
			//System.out.println("with distance of: "+ dist(cp[0],cp[1]));
		

	}
	
	//helper function for finising min distance between 2 sets of 2 schools
	public static School[] minDist(School[] a, School[] b) {
		School[] closestPair = new School[2];
		double distA = dist(a[0], a[1]);
		double distB = dist(b[0], b[1]);

		if (Math.min(distA, distB) == distA) {
			closestPair[0] = a[0];
			closestPair[1] = a[1];
			
		}
		else {
			closestPair[0] = b[0];
			closestPair[1] = b[1];
		}
		return closestPair;
           
	}
	//helper function to find dist
	public static double dist (School a, School b){
		return Math.sqrt( Math.pow((a.xpos - b.xpos), 2) +  Math.pow((a.ypos - b.ypos), 2));

	}

	public static School[] ClosestPoints(ArrayList<School> sLx, ArrayList<School> sLy){
		// Recursive divide and conquer algorithm for closest points
		// sLx should be sorted by x coordinate and sLy should be sorted by y coordinate
		// Returns an array containing the two closest School objects

		School[] closestPair = new School[2];

		///////////////////
		//Your code here!//
		//Brute force
		
		if (sLy.size()<=3) {
			closestPair[0] = sLy.get(0);
			closestPair[1] = sLy.get(1);
			double min = dist(closestPair[0], closestPair[1]); 

			for (int i = 0; i < sLx.size(); ++i) {
		        for (int j = i+1; j < sLx.size(); ++j) {
		            if (dist(sLy.get(i), sLy.get(j)) < dist(closestPair[0], closestPair[1])) { 
		                min = dist(sLy.get(i), sLy.get(j));  
						closestPair[0] = sLy.get(i);
						closestPair[1] = sLy.get(j);
		            }
		        }
			}
		    return closestPair;  
		}
		//ensure sorted by x...not sure if given this way?

		Collections.sort(sLx, new SortbyX());

		//find middle pt by X coord
		int mid = sLx.size()/2;
		
		//Divide into L and R
		//https://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
		ArrayList<School> LsLx = new ArrayList<School>(sLx.subList(0,mid));
		ArrayList<School> RsLx = new ArrayList<School>(sLx.subList(mid, sLx.size()));
		//Recursion
		School[] left = ClosestPoints(LsLx, LsLx);
		School[] right = ClosestPoints(RsLx, RsLx);
		
		//set delta 
		closestPair = minDist(left, right);
		double delta = dist(closestPair[0],closestPair[1]);
		//System.out.println(delta +"with schools of" +closestPair[0] +" and "+closestPair[1] );

		//sort via y pos
		Collections.sort(sLy, new SortbyY());
		
		//create array of schools nearby
		ArrayList<School> nearSchools = new ArrayList<School> (sLy.size()); 
		//find all pair smaller than delta
	    for (int i = 0; i < sLy.size(); i++)  {
	        if (Math.abs(sLy.get(i).xpos - sLy.get(mid).xpos) < delta) {
	            nearSchools.add(sLy.get(i));
	        }
	    }
		
		//look at next 7 points compared to each pt
	    //set delta to smallest
	    for (int i = 0; i < nearSchools.size(); ++i){
			int check = 7;
			//make sure only check next 7 if their are at least 7 more
			if(nearSchools.size()-i < 7){
				check = nearSchools.size()-i;
			}
	        for (int k = 1; k < check; k++) {
				if(dist(nearSchools.get(i),nearSchools.get(i+k))<delta){
					closestPair[0] = nearSchools.get(i);
					closestPair[1] = nearSchools.get(i+k); 
				}
	        	
			}
		}
				

		return closestPair;




	}

	

}
