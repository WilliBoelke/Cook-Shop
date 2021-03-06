package com.example.cookshop.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cookshop.R;
import com.example.cookshop.model.database.DatabaseHelper;
import com.example.cookshop.model.listManagement.AvailableListManager;
import com.example.cookshop.controller.applicationController.ApplicationController;
import com.example.cookshop.model.listManagement.RecipeListManager;
import com.example.cookshop.model.listManagement.ShoppingListManager;
import com.example.cookshop.model.listManagement.ToCookListManager;
import com.example.cookshop.view.SynchronizeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    //------------Static Variables------------


    //------------Instance Variables------------



    private  final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: Called");
        super.onCreate(savedInstanceState);

        //Set the Abb theme (true = dark, false = light)
        setTheme(false);
        //
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        ApplicationController.getInstance().initialize(this.getApplicationContext(), new RecipeListManager(db), new ShoppingListManager(db), new AvailableListManager(db), new ToCookListManager(db));

        //Setting the FragmentFactory
        getSupportFragmentManager().setFragmentFactory(new FragmentFactory());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentShoppingList.class, new Bundle()).addToBackStack(null).commit();
        setContentView(R.layout.activity_main);

        this.setupBottomNav();
    }


    //------------Setup Views------------

    private void setTheme(boolean theme)
    {
        //Setting the app heme and contentView
        Log.d(TAG, "onCreate : setting app theme...");
        if (theme)
        {
            setTheme(R.style.DarkTheme);
            Log.d(TAG, "onCreate :  app theme DARK");
        }
        else
        {
            setTheme(R.style.LightTheme);
            Log.d(TAG, "onCreate :  app theme LIGHT");
        }
    }

    private void setupBottomNav()
    {
        // setting up the BottomNavigationView with Listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }




    //------------Bottom Navigation ------------

    /**
     * Bottom Nav Listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Class selectedFragment = null;
            // switch ... case to select the right Fragment to start
            switch (item.getItemId())
            {
                case R.id.nav_to_cook:
                    Log.d(TAG, "onNavigationItemSelected: replacing current fragment with fragment toCook");
                    selectedFragment = FragmentToCookList.class;
                    break;
                case R.id.nav_sync:
                    Log.d(TAG, "onNavigationItemSelected: replacing current fragment with fragment sync");
                    selectedFragment = FragmentSyc.class;
                    break;
                case R.id.nav_recipes:
                    Log.d(TAG, "onNavigationItemSelected: replacing current fragment with fragment recipes");
                    selectedFragment = FragmentRecipeList.class;
                    break;
                case R.id.nav_available:
                    Log.d(TAG, "onNavigationItemSelected: replacing current fragment with fragment available");
                    selectedFragment = FragmentAvailableList.class;
                    break;
                case R.id.nav_shopping:
                    Log.d(TAG, "onNavigationItemSelected: replacing current fragment with fragment shopping");
                    selectedFragment = FragmentShoppingList.class;
                    break;
                default:
                    selectedFragment = FragmentShoppingList.class;
                    return false;
            }

            // giving the FragmentManager the container and the fragment which should be loaded into view
            // ... also commit
            try
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, new Bundle()).addToBackStack(null).commit();
            }
            catch (NullPointerException e)
            {
                Log.e(TAG, "onNavigationItemSelected: Fragment was null ", e);
            }
            // return true to tell that everything did go right
            return true;
        }
    };



    public void shoppingListSyncOnClick(View view)
    {
        Intent syncIntent = new Intent(getApplicationContext(), SynchronizeActivity.class);
        startActivity(syncIntent);
    }

    public void recipeListSyncOnClick(View view)
    {
        Toast toast=Toast.makeText(getApplicationContext(),"Not available at the moment",Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }
}
