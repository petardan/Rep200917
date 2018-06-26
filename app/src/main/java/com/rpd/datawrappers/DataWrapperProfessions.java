package com.rpd.datawrappers;

import com.rpd.customClasses.Profession;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Petar on 9/21/2017.
 */

public class DataWrapperProfessions implements Serializable {

    private ArrayList<Profession> professions;

    public DataWrapperProfessions(ArrayList<Profession> data) {
        this.professions = data;
    }

    public ArrayList<Profession> getParliaments() {
        return this.professions;
    }

}
