import javax.xml.crypto.Data;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class Main201413126{

    public static ArrayList<String> ReadData(String pathname) {
        ArrayList<String> strlist = new ArrayList<String>();
        try {

            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            int j = 0;
            String line = "";
            while ((line = br.readLine()) != null) {
                strlist.add(line);
            }
            return strlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strlist;
    }

    public static ArrayList<ArrayList<Integer> > DataWash(ArrayList<String> Datalist) {
        ArrayList<ArrayList<Integer> > AIS = new ArrayList<ArrayList<Integer> >();
        ArrayList<Integer> IS = new ArrayList<Integer>();
        for (int i = 0; i < Datalist.size(); i++) {
            String Tstr = Datalist.get(i);
            if (Tstr.equals("A") == false) {
                IS.add(Integer.parseInt(Tstr));
            }
            if (Tstr.equals("A")) {
                ArrayList<Integer> elemAIS = new ArrayList<Integer>(IS);
                AIS.add(elemAIS);
                IS.clear();
            }
        }
        return AIS;
    }



//%%%%%%%%%%%%%%%%%%%%%%% Procedure LongestComb() that will contain your code:

    public static int LongestComb(int[] A, int n){

        /*

        This is a sequentially-implemented algorithm to find the longest anchored comb. No recursion has been implemented.

        1) The algorithm checks if the array is decreasing. If it is decreasing or filled with the same integer throughout, k must be 0 because no teeth can form
        2) Next, the algorithm checks if the array is increasing. If it is increasing, k must be 1 because every pair in the array is an anchored comb of length 1
        3) Check if the array is too small to contain teeth i.e. if it is 0 or 1 integers in length
        4) Find the largest distance between two identical integers in the array. Store the positions of these two integers (add one to the final integer).
        5) Count the teeth between these two integers, using their stored positions.
        6) Not implemented: decrease the size of the array and check the next integer-largest comb for teeth count, and so on until all possible combs are checked.
        7) The largest teeth count found is returned.


        Time complexity: O(n^2). This is because at most, there is one 'for loop' nested inside another, and in this case each loop iterates n times. This results
        in a time complexity of n*n.

         */

        // check if array is decreasing, increasing, constant or too small

        for (int i = 0; i < n - 1; i++) {
            if (A[i] >= A[i+1]) { // check if decreasing or constant
                if (i == n - 2) return 0; // there is no anchored comb
            }
            else break;
        }

        for (int i = 0; i < n - 1; i++) {
            if (A[i] < A[i + 1]) { // check if increasing
                if (i == n - 2) return 1; // the largest anchored comb has length 1
            }
            else break;
        }

        // check if array is too small to contain any teeth
        if (n == 0 || n == 1) return 0;

        int k; // current tooth count
        int startComb; // start index of subsequence
        int endComb; // end index of subsequence
        int maxK = 0; // largest tooth count found thus far
        int[] indexes; // stores indices of duplicate numbers with largest distance between them

        for (int x = 0; x < 1; x++) {

            // find largest distance between duplicate values in A
            indexes = largestDuplicateDistance(A, n);

            // store first number in subsequence
            startComb = indexes[0];

            // store last number in subsequence
            endComb = indexes[1];

            n = endComb - startComb; // number of digits in subsequence

            // trim array to prepare it for getK
            A = trimArray(A, startComb, endComb, n);

            // count teeth in trimmed array
            k = getK(A, n);

            // if k is new largest k, we store it. Otherwise, disregard it
            if (k > maxK) maxK = k;

            // Ideally, the algorithm would now trim the array again to find the next largest possible comb.
            // Unfortunately, this did not work and so is not implemented.
            /*
             n = n - 2;

             if (startComb < endComb - 2) {
                 startComb++;
                 endComb--;
             }
             else return maxK;

            A = trimArray(A, startComb + 1, endComb - 1, n);
             */
        }
        return maxK;

    } // end of procedure LongestComb()



    // count teeth
    public static int getK(int[] A, int n) {

        int k = 0; // teeth count
        int checkValue = returnMax(A, n) + 1; // stores the second integer of a tooth, when there is a need to skip integer(s)
        // compare checkValue to each following integer to find the next tooth

        n++; // dealing with out of bounds exceptions

        // loop through the array
        for (int i = 0; i < n - 1; i++) {

            // if the second digit in the candidate tooth is greater than the first digit, and
            // the first digit is greater than the second digit of the previous tooth, increment k
            if (A[i] < A[i + 1] && A[i] < checkValue) {
                checkValue = A[i + 1];
                i++;
                k++;
            }
        }

        return k;
    }


    // trim larger array into smaller array
    public static int[] trimArray(int[] in, int start, int end, int n) {
        n++; // deal with out of bounds exceptions
        int[] out = new int[n]; // output array of size (end - start)
        int outIdx = 0; // index used to loop through output array
        int inIdx = start; // index used to loop through input array
        while (outIdx < n && inIdx < end + 1) { // add input array numbers into output array
            out[outIdx] = in[inIdx];
            outIdx++;
            inIdx++;
        }
        return out;
    }



    // return largest value in an array
    public static int returnMax(int[] A, int n){
        int max = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] > max) max = A[i];
        }
        return max;
    }


    // find largest distance between two identical numbers
    public static int[] largestDuplicateDistance(int[] A, int n) {

        int maxD = 0; // the currently known largest distance

        int first = 0; // position of first duplicate
        int last = 0; // position of final duplicate

        // if difference is greater than known max, update known max and store indexes
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (A[i] == A[j] && j - i > maxD) { // found currently known largest distance
                    maxD = j - i;
                    first = i;
                    last = j;
                }
            }
        }

        // last position is updated to include the second digit of the final tooth
        if (last != n - 1)  last++;

        // return array of first and last indexes
        return new int[] {first, last};
    }

    /*


    public static int[][] returnPairs(int[] A, int n) {

        int size = nthTriangleNumber(n - 1);
        int[][] outArray = new int[size][2];


        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (A[i] == A[j]) {
                    outArray = appendArray(A, n, new int[]{i, j});
                }
            }
        }

        return outArray;
    }


    public static int[] appendArray(int[] A, int n, int[] input) {
        int[] outArray = new int[n + 1];

        for (int i = 0; i < n; i++) {
            outArray[i] = A[i];
        }

        outArray[n] = input;

        return outArray;
    }




    public static int nthTriangleNumber(int n)
    {
        if (n <= 1) // base case
            return 1;
        else
            return n + nthTriangleNumber(n-1);
    }


     */





    // a failed attempt at recursion

    public static int recursiveK(int[] A, int n, int check) {

        if (A[0] < check && A[0] < A[1] && A[1] > A[2] && n > 1) {
            A = trimArray(A, 2, n, n - 2);
            return 1 + recursiveK(A, n-2, check);
        }
        else if (A[0] < check && A[0] < A[1] && n > 1) {
            A = trimArray(A, 3, n , n - 3);
            return 1 + recursiveK(A, n-3, check);
        }
        else {
            A = trimArray(A, 1, n, n - 1);
            return recursiveK(A, n-1, check);
        }
    }




    public static int[] searchArray(int[] A, int n, int searchN){
        int[] outArray = new int[n];
        for (int i = 0; i < n; i++) {
            outArray[i] = 0;
            if (A[i] == searchN) {
                outArray[i] = 1;
            }
        }
        return outArray;
    }





    public static int Computation(ArrayList<Integer> Instance, int opt){
        // opt=1 here means option 1 as in -opt1, and opt=2 means option 2 as in -opt2
        int NGounp = 0;
        int size = 0;
        int Correct = 0;
        size = Instance.size();

        int [] A = new int[size-opt];
        // NGounp = Integer.parseInt((String)Instance.get(0));
        NGounp = Instance.get(0); // NOTE: NGounp = 0 always, as it is NOT used for any purpose
        // It is just the first "0" in the first line before instance starts.
        for (int i = opt; i< size;i++ ){
            A[i-opt] = Instance.get(i);
        }
        int Size =A.length;
        if (NGounp >Size ){
            return (-1);
        }
        else {
            //Size
            int R = LongestComb(A,Size);
            return(R);
        }
    }

    public static String Test;


    public static void main(String[] args) {
        if (args.length == 0) {
            String msg = "Rerun with flag: " +
                    "\n\t -opt1 to get input from dataOne.txt " +
                    "\n\t -opt2 to check results in dataTwo.txt";
            System.out.println(msg);
            return;
        }
        Test = args[0];
        int opt = 2;
        String pathname = "dataTwo.txt";
        if (Test.equals("-opt1")) {
            opt = 1;
            pathname = "dataOne.txt";
        }


        ArrayList<String> Datalist = new ArrayList<String>();
        Datalist = ReadData(pathname);
        ArrayList<ArrayList<Integer> > AIS = DataWash(Datalist);

        int Nins = AIS.size();
        int NGounp = 0;
        int size = 0;
        if (Test.equals("-opt1")) {
            for (int t = 0; t < Nins; t++) {
                int Correct = 0;
                int Result = 0;
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
            }
        }
        else {
            int wrong_no = 0;
            int Correct = 0;
            int Result = 0;
            ArrayList<Integer> Wrong = new ArrayList<Integer>();
            for (int t = 0; t < Nins; t++) {
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
                Correct = Instance.get(1);
                if (Correct != Result) {
                    Wrong.add(t+1);
                    wrong_no=wrong_no+1;
                }
            }
            if (Wrong.size() > 0) {System.out.println("Index of wrong instance(s):");}
            for (int j = 0; j < Wrong.size(); j++) {
                System.out.print(Wrong.get(j));
                System.out.print(",");

                /*ArrayList Instance = (ArrayList)Wrong.get(j);
                for (int k = 0; k < Instance.size(); k++){
                    System.out.println(Instance.get(k));
                }*/
            }
            System.out.println("");
            System.out.println("Percentage of correct answers:");
            System.out.println(((double)(Nins-wrong_no) / (double)Nins)*100);

        }

    }
}