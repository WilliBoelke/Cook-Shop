package com.example.cookshop.items;


import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Superclass for {@link Article} and {@link Recipe}.
 * Provides the variables name, and description (and the corresponding getter and setter Methods)
 * which are used in both of the mentioned Classes.
 * <p>
 * Inherits from {@link Memento}  because all items should provide the methods {@link Memento#getMementoPattern()}
 * and {@link Memento#setObjectFromMementoPattern(String)}.
 * ---Notice: this may change in upcoming Versions, when we find a better solution to persist and transfer the objects.
 *
 * @author willi
 */
public abstract class Item implements Serializable, Parcelable, Memento
{
    /**
     * The Log Tag
     */
    private final String TAG =this.getClass().getSimpleName();
    /**
     * The name of the Item
     */
    protected String name;
    /**
     * The description of the item
     */
    protected String description;


    // ....Setter..........

    /**
     * Getter for the name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for the name
     */
    public void setName(String name) {
        this.name = name.trim();
    }


    // ....Getter..........

    /**
     * Getter for the description
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for the description
     * trims the String
     */
    public void setDescription(String description) {
        this.description = description.trim();
    }


    @Override
    public String toString() {
        return this.name;
    }


    public byte[] serialize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        return byteArrayOutputStream.toByteArray();

    }
}
