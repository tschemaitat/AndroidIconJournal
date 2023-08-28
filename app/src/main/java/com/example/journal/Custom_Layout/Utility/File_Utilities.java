package com.example.journal.Custom_Layout.Utility;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File_Utilities {
//    public static void get_path_string(File file, Context context){
//        File storage_dir = context.getFilesDir();
//        String storage_dir_string = storage_dir.getPath();
//        String file_string = file.getPath();
//        System.out.println("storage dir string: " + storage_dir_string);
//        System.out.println("file string: " + file_string);
//
//    }

    public static String get_directory_string(Context context){
        return context.getFilesDir().getPath();
    }

    public static void writeFile(String data, String file_path){

        File file = new File(file_path);
        boolean file_exists = false;
        //File file = new File(root, file_string);
        try {
            if(!file.exists())
                file.createNewFile();
            else
            {
                file_exists = true;
            }
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("writing to file: " + file_path + ", file already exists?: " + file_exists);
        System.out.println("path: " + file.getPath());
    }

    public static void writeFile_from_directory_folder(Context context, String data, String file_string) {

        File storage_dir = context.getFilesDir();
        //System.out.println("storage can write: " + storage_dir.canWrite());
        //System.out.println("storage can read: " + storage_dir.canRead());

//        File root = new File(context.getFilesDir(), folder);
//        // if external memory exists and folder with name YourAppFolderName
//        if (!root.exists()) {
//            boolean worked = root.mkdirs(); // this will create folder.
//            if(!worked)
//                throw new RuntimeException();
//        }
        File file = new File(context.getFilesDir(), file_string);
        boolean file_exists = false;
        //File file = new File(root, file_string);
        try {
            if(!file.exists())
                file.createNewFile();
            else
            {
                file_exists = true;
            }
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("writing to file: " + file_string + ", file already exists?: " + file_exists);
        System.out.println("path: " + file.getPath());
    }

    public static void print_files(Context context){
        print_files(context.getFilesDir(), 0);
    }

    public static void print_files(File file, int tabs){
        for(int i = 0; i < tabs; i++){
            System.out.print("\t");
        }
        System.out.println(file.getName());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                print_files(files[i], tabs + 1);
            }
        }
    }

    public static String readFile(String file_string) {
        StringBuilder text = new StringBuilder();
        //File root = new File(context.getFilesDir(), folder);
        File file = new File(file_string);
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

    public static String readFile_from_directory(Context context, String file_string) {
        StringBuilder text = new StringBuilder();
        //File root = new File(context.getFilesDir(), folder);
        File file = new File(context.getFilesDir(), file_string);
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

    public static File[] files_in_directory(File directory, Context context){
        File file = directory;//new File(context.getFilesDir(), directory_string);
        if(!file.isDirectory()){
            System.out.println("file: " + file.getPath() + ", is not a directory");
            throw new RuntimeException();
        }
        File[] files = file.listFiles();
        String[] strings = new String[files.length];
        for(int i = 0; i < files.length; i++){
            strings[i] = files[i].getPath();
        }
        return files;
    }
}
