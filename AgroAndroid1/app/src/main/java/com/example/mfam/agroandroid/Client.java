package com.example.mfam.agroandroid;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;

import static android.os.SystemClock.sleep;

public class Client extends AsyncTask<String, String[], String[]> {
    private Socket s;
    private String[] sp;
    private String ip = "192.168.1.102";

    public String[] getSp(){

        //int timeOut = 0;
        //while (sp == null || timeOut < 50){
        while (sp == null){
            sleep(50);
            //timeOut++;
        }
        return sp;
    }

    @Override
    protected void onProgressUpdate(String[]... values) {
        System.setProperty("console.encoding","Cp866");
        super.onProgressUpdate(values);
        sp = Arrays.deepToString(values[0]).split(", ");
    }
    @Override
    protected void onPostExecute(String[] result) {
        System.setProperty("console.encoding","Cp866");
        super.onPostExecute(result);

        System.out.println("Execute ________________ "+ Arrays.toString(result));
        sp = Arrays.deepToString(result).split(", ");
    }

    //tinker

    @Override
    protected String[] doInBackground(String... params) {
        System.setProperty("console.encoding","Cp866");
        try {
            String message = params[0];
            s = new Socket(ip, 27016);
            String result = writeToAndReadFromSocket(s, message+"\n\n");
            // print out the result we got back from the server
            System.out.println(result);
            sp = result.split("&");
            publishProgress(sp);
            s.close();
            return sp;
        } catch (IOException e) {
            System.out.println("________________________________________error__________________________________________________");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private String writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception {
        System.setProperty("console.encoding","Cp866");
        try
        {
            // write text to the socket
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(writeTo);
            bufferedWriter.flush();

            // read text from the socket
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null)
            {
                sb.append(str);
            }

            // close the reader, and return the results as a String
            bufferedReader.close();
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }


}

