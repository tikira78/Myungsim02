package com.msba.myungsim02.record;

public class RecordItem {
    int SBP;
    int DBP;
    int HR;
    int BST;
    float WEIGHT;
    long DATE1;
    long DATE2;
    String DATE;

    public RecordItem(int SBP, int DBP, int HR, int BST, float WEIGHT, long DATE1, long DATE2, String DATE) {
        this.SBP = SBP;
        this.DBP = DBP;
        this.HR = HR;
        this.BST = BST;
        this.WEIGHT = WEIGHT;
        this.DATE1 = DATE1;
        this.DATE2 = DATE2;
        this.DATE = DATE;

    }

    public float getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(float WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public long getDATE1() {
        return DATE1;
    }

    public void setDATE1(long DATE1) {
        this.DATE1 = DATE1;
    }

    public long getDATE2() {
        return DATE2;
    }

    public void setDATE2(long DATE2) {
        this.DATE2 = DATE2;
    }

    public int getSBP() {
        return SBP;
    }

    public void setSBP(int SBP) {
        this.SBP = SBP;
    }

    public int getDBP() {
        return DBP;
    }

    public void setDBP(int DBP) {
        this.DBP = DBP;
    }

    public int getHR() {
        return HR;
    }

    public void setHR(int HR) {
        this.HR = HR;
    }

    public int getBST() {
        return BST;
    }

    public void setBST(int BST) {
        this.BST = BST;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
