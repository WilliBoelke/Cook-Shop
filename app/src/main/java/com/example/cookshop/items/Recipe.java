package com.example.cookshop.items;

import android.os.Parcel;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * Subclass of {@link Item}.
 * A recipe represents a item in the the recipe list
 * <p>
 * An Recipe has (in addition to {@link Item} ) a list of {@link Article}
 * and a list of Steps
 *
 * @author will
 * @version 1.0
 */
public class Recipe extends Item implements Comparable<Recipe> , Serializable
{
    /**
     *
     */
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>()
    {
        @Override
        public Recipe createFromParcel(Parcel in)
        {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size)
        {
            return new Recipe[size];
        }
    };
    private  final String TAG =  this.getClass().getSimpleName();

    /**
     * the list of the Ingredients
     */
    private ArrayList<Article> articles;

    public Boolean getOnToCook()
    {
      return onToCook;
    }

    public void setOnToCook(Boolean onToCook)
    {
      this.onToCook = onToCook;
  }

     private Boolean onToCook;


    //....Constructor..........
    /**
     * The list containing the  {@link Step}s
     */
    private ArrayList<Step> steps;


    /**
     * Constructor
     *
     * @param name
     * @param description
     * @param ingreList
     */
    public Recipe(String name, String description, ArrayList<Article> ingreList, ArrayList<Step> stepsList)
    {
        this.setName(name);
        this.setDescription(description);
        this.setArticles(ingreList);
        this.setSteps(stepsList);
        if(onToCook==null){this.setOnToCook(false);}
    }

    /**
     * Constructor to set an Recipe from a memento pattern
     *
     * @param mementoPattern
     *         the memento pattern
     * @see Memento
     */
    public Recipe(String mementoPattern)  throws  NoSuchElementException
    {
        Log.e(TAG, "Constructor  called with " + mementoPattern);
        setObjectFromMementoPattern(mementoPattern);
    }

    Recipe()
    {

    }


    //....Methods..........


    /**
     * Constructor to set an Recipe from a {@link Parcel}
     *
     * @param in
     */
    protected Recipe(Parcel in)
    {
        name = in.readString();
        description = in.readString();
        articles = in.createTypedArrayList(Article.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        onToCook = in.readByte()!=0;
    }


    //....Setter..........

    /**
     * Method to compare two objects of this class by their name
     *
     * @param other
     *         another object of this class
     * @return
     */
    @Override
    public int compareTo(Recipe other)
    {
        if (this.getName().compareTo(other.getName()) < 0)
        {
            return 1;
        }
        if (this.getName().compareTo(other.getName()) > 0)
        {
            return -1;
        }
        return 0;
    }

    /**
     * @return
     */
    public ArrayList<Article> getArticles()
    {
        return articles;
    }


    //....Getter..........

    /**
     * @param articles
     */
    public void setArticles(ArrayList<Article> articles)
    {
        this.articles = articles;
    }

    /**
     * @return
     */
    public ArrayList<Step> getSteps()
    {
        return steps;
    }


    //....Memento.........

    /**
     * @param steps
     */
    public void setSteps(ArrayList<Step> steps)
    {
        this.steps = steps;
    }

    /**
     * @return
     */
    @Override
    public String getMementoPattern()
    {
        String        mementoPattern = "";
        StringBuilder sb             = new StringBuilder(mementoPattern);

        sb.append(this.name + Memento.DELIMITER_RECIPES);
        sb.append(this.description + Memento.DELIMITER_RECIPES);
        for (int i = 0; i < this.articles.size(); i++)
        {
            sb.append(articles.get(i).getMementoPattern());
            sb.append(Memento.DELIMITER_INTER_ARTICLE);
        }
        sb.append(Memento.DELIMITER_RECIPES);
        for (int i = 0; i < this.steps.size(); i++)
        {
            sb.append(steps.get(i).getMementoPattern());
            sb.append(Memento.DELIMITER_INTER_STEP);
        }
        sb.append(Memento.DELIMITER_RECIPES);
        return sb.toString();
    }


    //....Parcelable..........

    /**
     * @param mementoPattern
     */
    @Override
    public  void setObjectFromMementoPattern(String mementoPattern) throws NoSuchElementException
    {
        Log.e(TAG, "setObjectFromMementoPattern  called with " + mementoPattern);

        if (mementoPattern.trim().length() > 0)
        {
            StringTokenizer st = new StringTokenizer(mementoPattern, Memento.DELIMITER_RECIPES);
            this.setName(st.nextToken(Memento.DELIMITER_RECIPES));
            this.setDescription(st.nextToken(Memento.DELIMITER_RECIPES));

            this.onToCook = false; // Always false, because thats just used for import and export ...and in that case we don't care if the other person has it on his toCook list
            this.articles = new ArrayList<>();
            this.steps = new ArrayList<>();


            String articles = st.nextToken(Memento.DELIMITER_RECIPES);
            Log.e(TAG, "setObjectFromMementoPattern:  articles : " + articles);
            StringTokenizer ast = new StringTokenizer(articles, Memento.DELIMITER_INTER_ARTICLE);
            while (ast.hasMoreTokens())
            {
                //Create Article from its own memento pattern and add it to the list
                this.articles.add(new Article(ast.nextToken(Memento.DELIMITER_INTER_ARTICLE).trim()));
            }


            String steps = st.nextToken(Memento.DELIMITER_RECIPES);
            Log.e(TAG, "setObjectFromMementoPattern:  Steps : " + articles);
            StringTokenizer sst = new StringTokenizer(steps, Memento.DELIMITER_INTER_STEP);
            while (sst.hasMoreTokens())
            {
                //Create Article from its own memento pattern and add it to the list
                this.steps.add(new Step(sst.nextToken(Memento.DELIMITER_INTER_STEP).trim()));
            }
        }
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(articles);
        dest.writeTypedList(steps);
        dest.writeByte((byte) (onToCook ? 1 : 0)); //1 when true
    }

    public static Recipe deserialize(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream    o = new ObjectInputStream(b);
        return   (Recipe) o.readObject();
    }
}
