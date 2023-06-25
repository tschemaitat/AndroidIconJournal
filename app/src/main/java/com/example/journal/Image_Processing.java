package com.example.journal;


import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableKt;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.nio.file.Files;
import java.io.*;
import java.util.*;


public class Image_Processing {


    //private static Context my_context;
    private static List<Bitmap> save_drawables;
    public static final int icon_draw_width = 200;
    public static final int icon_picture_width = 140;
    public static void rescale(){

    }
    public static void setup(Context context){
        //save_drawables = new ArrayList<>();
        //my_context = context;
    }

    public static Bitmap resizeBitmap(int resource, int targetW, int targetH, Context context) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resource);
        if(icon == null)
            return null;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = icon.getWidth();
        int photoH = icon.getHeight();
        double scaleFactor = 1;
//        if ((targetW > 0) || (targetH > 0)) {
//            if(((photoW*1.0) / targetW) > 1.0 || ((photoH*1.0) / targetH) > 1.0)
//                scaleFactor = Math.max(photoW*1.0/targetW, photoH*1.0/targetH);
//            else
//                scaleFactor =  Math.min(photoW/targetW, photoH/targetH);
//        }

        float target_ratio_w = (targetW * 1.0f) / photoW;
        float target_ratio_h = (targetH * 1.0f) / photoH;


        //if both are too small
        //scale is based on the one that needed the big scale

        //if one is too small
        //scale is based on the small

        //if both too big
        //scale is based on the big scale

        scaleFactor = Math.max(target_ratio_w, target_ratio_h);

        Bitmap bm = Bitmap.createScaledBitmap(icon, (int)(photoW*scaleFactor), (int)(photoH*scaleFactor), true);
        //bm = invert(bm);

        //BitmapFactory.decodeFile(photoPath, bmOptions);



        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = (int)scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return bm;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap invert(Bitmap src)
    {
        int height = src.getHeight();
        int width = src.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        ColorMatrix matrixGrayscale = new ColorMatrix();
        matrixGrayscale.setSaturation(0);

        ColorMatrix matrixInvert = new ColorMatrix();
        matrixInvert.set(new float[]
                {
                        -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                });
        matrixInvert.preConcat(matrixGrayscale);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixInvert);
        paint.setColorFilter(filter);

        canvas.drawBitmap(src, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap cropBitmap_centerAnchor(Bitmap bitmap, int finalWidth, int finalHeight) {
        // Calculate the starting points for the crop
        int startX = (bitmap.getWidth() - finalWidth) / 2;
        int startY = (bitmap.getHeight() - finalHeight) / 2;

        // Check if startX and startY are negative. If they are, set them to 0
        startX = Math.max(startX, 0);
        startY = Math.max(startY, 0);

        // Make sure the end points of the crop are within the bitmap
        finalWidth = startX + finalWidth <= bitmap.getWidth() ? finalWidth : bitmap.getWidth() - startX;
        finalHeight = startY + finalHeight <= bitmap.getHeight() ? finalHeight : bitmap.getHeight() - startY;

        // Crop the bitmap
        return Bitmap.createBitmap(bitmap, startX, startY, finalWidth, finalHeight);
    }

    public static List<Bitmap> drawables(Context my_context){
//        if(save_drawables != null)
//            return save_drawables;
        save_drawables = new ArrayList<>();
        int[] drawable_ids = {
                R.drawable.cat,
                R.drawable.cow
        };
        Bitmap[] bitmaps = new Bitmap[drawable_ids.length];
        for(int i = 0; i < drawable_ids.length; i++){
            bitmaps[i] = resizeBitmap(drawable_ids[i], icon_picture_width, icon_picture_width ,my_context);
            bitmaps[i] = cropBitmap_centerAnchor(bitmaps[i], icon_picture_width, icon_picture_width);
            bitmaps[i] = getRoundedCornerBitmap(bitmaps[i], 50);

//            Picasso.get().load("http://your.image.url").transform(new RoundedCorn(10, 0)).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    // Convert bitmap to drawable
//                    Drawable drawable = new BitmapDrawable(my_context.getResources(), bitmap);
//
//                    // Use the drawable
//                }
//
//                @Override
//                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                    // Handle the failure
//                    System.out.println("TAG"+"Failed to load the image");
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    // This method is invoked right before your request is submitted.
//                }
//            });
            if(bitmaps[i] == null)
                System.out.println("error getting drawable: " + i);
            else
                save_drawables.add(bitmaps[i]);
        }
        return save_drawables;
    }



    public static void get_drawables_from_directory(Context my_context){
        File directory = my_context.getFilesDir();

        System.out.println(directory);
        File file = directory;//new File(directory);


        System.out.println("directory exits: " + file.exists() + ", " + file.isDirectory());
        File[] children = file.listFiles();
        System.out.println("children null: " + (children == null));
        String child_name = children[0].getName();
        System.out.println("children: " + children.length);
        filter_to_png(children);
        System.out.println("#of pngs: " + children.length);


//        System.out.println(my_context.getResources().getResourceTypeName(R.drawable.bike) + ", " +
//                my_context.getResources().getResourceTypeName(R.drawable.bike) + ", " +
//                my_context.getResources().getResourcePackageName(R.drawable.bike));
        int starting_drawable = 700000;

        int index = 0;
        boolean has_next_drawable = true;
        Bitmap drawable_bitmap;
        while(has_next_drawable){
            drawable_bitmap = resizeBitmap(starting_drawable + index, icon_draw_width,icon_draw_width, my_context);
            if(drawable_bitmap == null)
                has_next_drawable = false;
            else
                index++;
        }
        System.out.println("number of drawables: " + index);
    }

    public static void filter_to_png(File[] files){
        List<File> list = new ArrayList<>();
        String name;
        for(int i = 0; i < files.length; i++){
            name = files[i].getName();
            name = name.substring(name.length() - 4);
            if(name.equals(".png")){
                list.add(files[i]);
            }
        }
        files = new File[list.size()];
        for(int i = 0; i < list.size(); i++){
            files[i] = list.get(i);
        }
    }



    public static byte[] extractBytes (String ImageName) {
        System.out.println("extracting bytes");
        List<Byte> byte_list = new ArrayList<>();
        int num = 0;
        int nums_read = 0;
        byte[] bytes = intToByteArray(num);

        // open image
        File file = new File(ImageName);
        file.setReadable(true);

        Scanner scan = null;
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("failed making file reader");
        }
        if(reader == null){
            System.out.println("Could not cast file to reader");
            return null;
        }
        System.out.println(scan.hasNextByte());
        Byte b = null;
        while(scan.hasNext()){
            System.out.print("scanning");
            b = null;
            if(nums_read %1000 == 0)
                System.out.println(nums_read);
            //System.out.println("starting read");
            b = scan.nextByte();

            nums_read++;
            if(b == null)
                System.out.println("error");
        }
        byte_list.add(b);
        byte[] result = byte_list_to_array(byte_list);
        printArray(result);
        System.out.println("\n" + nums_read);


        return bytes;
        // get DataBufferBytes from Raster
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static void printArray(byte[] bytes){
        System.out.print("{");
        if(bytes == null){
            System.out.println("array null");
            return;
        }
        for(byte b: bytes)
            System.out.print(b+", ");
        System.out.print("} ");
    }

    public static byte[] byte_list_to_array(List<Byte> list){
        byte[] result = new byte[list.size()];
        for(int i = 0; i < list.size(); i++)
            result[i] = list.get(i).byteValue();
        return result;
    }
}
