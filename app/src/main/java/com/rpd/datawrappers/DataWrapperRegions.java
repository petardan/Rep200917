package com.rpd.datawrappers;

import com.rpd.customClasses.Region;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Petar on 9/21/2017.
 */

public class DataWrapperRegions implements Serializable {

private ArrayList<Region> regions;

public DataWrapperRegions(ArrayList<Region> data) {
        this.regions = data;
        }

public ArrayList<Region> getParliaments() {
        return this.regions;
        }

        }
