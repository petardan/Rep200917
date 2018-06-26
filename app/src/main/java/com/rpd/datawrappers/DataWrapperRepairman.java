package com.rpd.datawrappers;

import com.rpd.customClasses.Repairman;

import java.io.Serializable;

/**
 * Created by Petar on 11/15/2017.
 */

public class DataWrapperRepairman implements Serializable {

    private Repairman repairman;

    public DataWrapperRepairman(Repairman data) {
        this.repairman = data;
    }

    public Repairman getParliaments() {
        return this.repairman;
    }

}
