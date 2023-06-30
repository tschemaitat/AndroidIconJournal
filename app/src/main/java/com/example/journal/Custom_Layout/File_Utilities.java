package com.example.journal.Custom_Layout;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File_Utilities {
    static String folder = "Journal_Data";
    static String file_string = "Journal_1.txt";

    public static void writeFile(Context context, String data) {
        File storage_dir = Environment.getExternalStorageDirectory();
        System.out.println("storage can write: " + storage_dir.canWrite());
        System.out.println("storage can read: " + storage_dir.canRead());

        File root = new File(context.getFilesDir(), folder);
        // if external memory exists and folder with name YourAppFolderName
        if (!root.exists()) {
            boolean worked = root.mkdirs(); // this will create folder.
            if(!worked)
                throw new RuntimeException();
        }
        File file = new File(root, file_string);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context) {
        StringBuilder text = new StringBuilder();
        File root = new File(context.getFilesDir(), folder);
        File file = new File(root, file_string);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
