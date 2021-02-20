package com.example.cookshop.view.sharing;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookshop.R;
import com.example.cookshop.controller.applicationController.ApplicationController;
import com.example.cookshop.items.Recipe;
import com.example.cookshop.model.UserPreferences;
import com.example.cookshop.view.adapter.RecipeRecyclerViewAdapter;

import java.util.NoSuchElementException;

public class ExportImportActivity extends AppCompatActivity
{
    private String TAG  = this.getClass().getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager  recyclerLayoutManager;
    private RecipeRecyclerViewAdapter recyclerAdapter;
    private EditText importEditText;
    private String currentMementoPatter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_export_import);
        importEditText = findViewById(R.id.import_edittext);
        setupRecyclerView();
    }


    protected void setupRecyclerView()
    {
        recyclerView = findViewById(R.id.recipe_export_recycler);
        if(recyclerView == null)
        {
            Log.e(TAG, "the rv is null");
        }
        recyclerLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerAdapter = new RecipeRecyclerViewAdapter(ApplicationController.getInstance().getRecipeList(), getApplicationContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        //nnClickListener

        recyclerAdapter.setOnItemClickListener(new RecipeRecyclerViewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                setExportPatterToTextView(ApplicationController.getInstance().getRecipe(position).getMementoPattern());
            }
        });
    }

    private void setExportPatterToTextView(String patter)
    {
        currentMementoPatter = patter;
        EditText editText = findViewById(R.id.export_edittext);
        editText.setText(patter);
    }


    public void importOnClick(View view)
    {
        try
        {
            Recipe newRecipe = new Recipe(importEditText.getText().toString());
            ApplicationController.getInstance().addRecipe(newRecipe);
        }
        catch (NoSuchElementException e)
        {
            Log.e(TAG, "The given pattern didnt match the requirements " + e.getMessage() + e.getStackTrace());
            Toast.makeText(getApplicationContext(), "The given pattern isnt valid", Toast.LENGTH_SHORT).show();
        }
    }


    public void copyButtonOnClick(View view)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("to cook recipe", currentMementoPatter);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }


    private void setTheme()
    {
        String theme = UserPreferences.getInstance().getTheme();

        switch (theme)
        {
            case UserPreferences.DARK_MODE:
                setTheme(R.style.DarkTheme);
                Log.d(TAG, "setTheme :  app theme DARK");
                break;
            case UserPreferences.LIGHT_MODE:
                setTheme(R.style.LightTheme);
                Log.d(TAG, "setTheme :  app theme LIGHT");
                break;
            case UserPreferences.LILAH_MODE:
                setTheme(R.style.LilahTheme);
                Log.d(TAG, "setTheme :  app theme LILAH");
                break;
            default:
                setTheme(R.style.DarkTheme);
                Log.d(TAG, "setTheme :  default DARK");
                break;

        }
    }


}