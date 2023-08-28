package com.example.journal.Custom_Layout.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.R;

import static com.example.journal.Custom_Layout.Utility.Image_Processing.*;

import java.util.ArrayList;
import java.util.List;

public class Drawable_Manager {

    private static ArrayList<Drawable_With_Data> drawable_with_data = new ArrayList<>();
    public static void drawables(Context my_context){
        ArrayList<Drawable> drawables = new ArrayList<>();
        List<Bitmap> save_bitmaps = new ArrayList<>();
        save_bitmaps = new ArrayList<>();
        String[] drawable_names = {
                "amusement_park",
                "apple",
                "banana",
                "beach",
                "bed",
                "bike",
                "birthday_cake",
                "boardgame",
                "breakfast",
                "bus",
                "car",
                "cat",
                "city",
                "coffee",
                "coffee_happy",
                "coffee_heart",
                "computer",
                "computer_2",
                "cow",
                "deck_of_cards",
                "dice",
                "dog",
                "dog_2",
                "dumbbell",
                "fish",
                "fish_black_and_white",
                "game",
                "garden",
                "grapes",
                "heart",
                "house",
                "iced_coffee",
                "milk",
                "motorcycle",
                "park",
                "party",
                "sandwhich",
                "slice_of_cake",
                "stove",
                "sushi",
                "train",
                "treadmill",
                "turkey",
                "tv",
                "zoo"
        };

        int[] drawable_ids = {
                R.drawable.amusement_park,
                R.drawable.apple,
                R.drawable.banana,
                R.drawable.beach,
                R.drawable.bed,
                R.drawable.bike,
                R.drawable.birthday_cake,
                R.drawable.boardgame,
                R.drawable.breakfast,
                R.drawable.bus,
                R.drawable.car,
                R.drawable.cat,
                R.drawable.city,
                R.drawable.coffee,
                R.drawable.coffee_happy,
                R.drawable.coffee_heart,
                R.drawable.computer,
                R.drawable.computer_2,
                R.drawable.cow,
                R.drawable.deck_of_cards,
                R.drawable.dice,
                R.drawable.dog,
                R.drawable.dog_2,
                R.drawable.dumbbell,
                R.drawable.fish,
                R.drawable.fish_black_and_white,
                R.drawable.game,
                R.drawable.garden,
                R.drawable.grapes,
                R.drawable.heart,
                R.drawable.house,
                R.drawable.iced_coffee,
                R.drawable.milk,
                R.drawable.motorcycle,
                R.drawable.park,
                R.drawable.party,
                R.drawable.sandwhich,
                R.drawable.slice_of_cake,
                R.drawable.stove,
                R.drawable.sushi,
                R.drawable.train,
                R.drawable.treadmill,
                R.drawable.turkey,
                R.drawable.tv,
                R.drawable.zoo
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

    public static Drawable_With_Data get_drawable(String name){
        for(int i = 0; i < drawable_with_data.size(); i++){
            if(drawable_with_data.get(i).name.equals(name))
                return drawable_with_data.get(i);
        }
        return null;
    }

    public static ArrayList<Drawable_With_Data>get_drawables(){
        return drawable_with_data;
    }

    public static Drawable_With_Data get_drawable_from_id(int id){
        for(int i = 0; i < drawable_with_data.size(); i++){
            if(drawable_with_data.get(i).id == id)
                return drawable_with_data.get(i);
        }
        return null;
    }
}
