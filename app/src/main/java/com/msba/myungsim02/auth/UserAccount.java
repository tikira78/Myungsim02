package com.msba.myungsim02.auth;

public class UserAccount {

    private String idToken;
    private String emailId;
    private String password;
    private String extime;
    private String dischargeDate;
    private String id;
    private String restinghr;
    private String setTime,getTime;
    private int steps, age;
    private int totalSteps;
    private String feedback;
    private boolean check;
    private String point;
    private String lee,hong;
    private String name;
    private String registerdate;

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public UserAccount() {
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLee() {
        return lee;
    }

    public void setLee(String lee) {
        this.lee = lee;
    }

    public String getHong() {
        return hong;
    }

    public void setHong(String hong) {
        this.hong = hong;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getRestinghr() {
    return restinghr;}

    public void setRestinghr(String restinghr) { this.restinghr = restinghr;}

    public String getId() { return id;}

    public void setId(String id) {this.id = id;}

    public String getDischargeDate() {  return dischargeDate; }

    public void setDischargeDate(String dischargeDate) { this.dischargeDate = dischargeDate; }

    public String getExtime() { return extime; }

    public void setExtime(String extime) {this.extime = extime;}

    public String getIdToken() { return idToken;    }

    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmailId() {return emailId;}

    public void setEmailId(String emailId) { this.emailId = emailId;}

    public String getPassword() { return password;}

    public void setPassword(String password) {this.password = password;}
}
