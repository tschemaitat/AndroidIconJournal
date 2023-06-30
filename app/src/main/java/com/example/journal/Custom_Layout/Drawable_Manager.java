package com.example.journal.Custom_Layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.journal.R;

import static com.example.journal.Image_Processing.*;

import java.util.ArrayList;
import java.util.List;

public class Drawable_Manager {

    private static ArrayList<Drawable_With_Data> drawable_with_data = new ArrayList<>();
    public static void drawables(Context my_context){
        ArrayList<Drawable> drawables = new ArrayList<>();
        List<Bitmap> save_bitmaps = new ArrayList<>();
        save_bitmaps = new ArrayList<>();
        String[] drawable_names = {
                "cat",
                "cow"
        };
        int[] drawable_ids = {
                R.drawable.cat,
                R.drawable.cow
        };

        Bitmap[] bitmaps = new Bitmap[drawable_ids.length];
        for(int i = 0; i < drawable_ids.length; i++){
            bitmaps[i] = resizeBitmap(drawable_ids[i], icon_picture_width, icon_picture_width ,my_context);
            bitmaps[i] = cropBitmap_centerAnchor(bitmaps[i], icon_picture_width, icon_picture_width);
            bitmaps[i] = getRoundedCornerBitmap(bitmaps[i], icon_picture_width/4);

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
                save_bitmaps.add(bitmaps[i]);
        }

        drawables = new ArrayList<>();
        for(int i = 0; i < save_bitmaps.size(); i++){
            Drawable d = new BitmapDrawable(my_context.getResources(), save_bitmaps.get(i));
            drawables.add(d);
        }
        for(int i = 0; i < drawables.size(); i++){
            drawable_with_data.add(new Drawable_With_Data(drawables.get(i), drawable_names[i], drawable_ids[i]));
        }
    }

    public static int size(){
        return drawable_with_data.size();
    }

    public static Drawable_With_Data get_drawable(int index){
        return drawable_with_data.get(index);
    }

    public static Drawable_With_Data get_drawable_from_id(int id){
        for(int i = 0; i < drawable_with_data.size(); i++){
            if(drawable_with_data.get(i).id == id)
                return drawable_with_data.get(i);
        }
        return null;
    }
}
