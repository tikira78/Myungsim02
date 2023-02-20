package com.msba.myungsim02.exercise;

public class ExdaItem {
    String TYPE;
    int HOUR;
    int MIN;
    int RPE;
    long DATE2;
    String DATE;

    public ExdaItem(String TYPE, int HOUR, int MIN, int RPE, long DATE2, String DATE) {
        this.TYPE = TYPE;
        this.HOUR = HOUR;
        this.MIN = MIN;
        this.RPE = RPE;
        this.DATE2 = DATE2;
        this.DATE = DATE;

    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getHOUR() {
        return HOUR;
    }

    public void setHOUR(int HOUR) {
        this.HOUR = HOUR;
    }

    public int getMIN() {
        return MIN;
    }

    public void setMIN(int MIN) {
        this.MIN = MIN;
    }

    public int getRPE() {
        return RPE;
    }

    public void setRPE(int RPE) {
        this.RPE = RPE;
    }

    public long getDATE2() {
        return DATE2;
    }

    public void setDATE2(long DATE2) {
        this.DATE2 = DATE2;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
