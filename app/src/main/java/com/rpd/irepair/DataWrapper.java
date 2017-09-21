package com.rpd.irepair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Petar on 9/21/2017.
 */

public class DataWrapper implements Serializable {

    private ArrayList<Profession> professions;

    public DataWrapper(ArrayList<Profession> data) {
        this.professions = data;
    }

    public ArrayList<Profession> getParliaments() {
        return this.professions;
    }

}
