package com.example.myapplication;


import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;


import com.google.android.gms.vision.text.TextBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextRecognized
{
    final static String fileName = "data.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
    final static String TAG = TextRecognized.class.getName();
    SparseArray<TextBlock> textBlocks;
    String text;

    boolean saveResult(String text)
    {
        try
        {
            new File(path).mkdir();
            File file = new File(path + fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((text + System.getProperty("line.separator")).getBytes());

            return true;
        }
        catch (FileNotFoundException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        catch (IOException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        return false;
    }

    public void loadResult()
    {
        String line = null;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(path + fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch (FileNotFoundException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        catch (IOException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        text = line;
    }
}
