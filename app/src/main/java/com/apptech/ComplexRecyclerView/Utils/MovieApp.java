package com.apptech.ComplexRecyclerView.Utils;

import android.widget.ImageView;

import com.apptech.apptechcomponents.R;

import java.util.ArrayList;

/**
 * Created by nirob on 7/8/17.
 */

public class MovieApp {

    public static MovieApp instance;

    public static MovieApp getInstance() {
        if (instance == null) {
            synchronized (MovieApp.class) {
                if (instance == null) {
                    instance = new MovieApp();
                }
            }
        }
        return instance;
    }

    private MovieApp(){
    }

    private ArrayList<Object> movieList;

    public ArrayList<Object> getCarList() {
        return movieList;
    }

    public void setCarList(ArrayList<Object> carList) {
        this.movieList = carList;
    }


    public  void setMovieImage(int position, ImageView imageView) {
        switch (position) {
            case 1:
                imageView.setBackgroundResource(R.drawable.dawn_of_planet_of_the_apes);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.distict9);
                break;

            case 3:
                imageView.setBackgroundResource(R.drawable.transformers);
                break;

            case 4:
                imageView.setBackgroundResource(R.drawable.xman);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.the_machinist);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.the_last_samurai);
                break;

            case 7:
                imageView.setBackgroundResource(R.drawable.spiderman);
                break;

            case 8:
                imageView.setBackgroundResource(R.drawable.tangled);
                break;

            case 9:
                imageView.setBackgroundResource(R.drawable.rush);
                break;

            case 10:
                imageView.setBackgroundResource(R.drawable.drag_me_to_hell);
                break;

            case 11:
                imageView.setBackgroundResource(R.drawable.despicable_me);
                break;

            case 12:
                imageView.setBackgroundResource(R.drawable.kill_bill);
                break;

            case 13:
                imageView.setBackgroundResource(R.drawable.a_bugs_life);
                break;

            case 14:
                imageView.setBackgroundResource(R.drawable.life_of_brian);
                break;

            case 15:
                imageView.setBackgroundResource(R.drawable.how_to_train_your_dragon);
                break;

            default:
                break;
        }
    }
}
