package com.mj.lift.transfer;

public enum  SensorDataSourceLocation {
    WRIST(0x01),
    WAIST(0x02),
    CHEST(0x03),
    FOOT(0x04),
    ANY(0x7f);

    private int value;
    private SensorDataSourceLocation(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}

