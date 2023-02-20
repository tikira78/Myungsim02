package com.msba.myungsim02.CPET;

public class CPETitem {

    String IMAGE;
    long DATE2;
    String DATE;

    public CPETitem(String IMAGE, long DATE2, String DATE) {
        this.IMAGE = IMAGE;
        this.DATE2 = DATE2;
        this.DATE = DATE;

    }

    public String getImage() {
        return IMAGE;
    }

    public void setImage(String IMAGE) {
        this.IMAGE = IMAGE;
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