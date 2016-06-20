package app_objects;

import json_stuff.AppJsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Player implements AppJsonObject {

    protected String name;
    protected String bio;
    protected String photoDone;
    protected String position;
    protected String specialPlayer;
    protected String playerNumber;
    protected String caps;
    protected String goalsForCountry;
    protected String club;
    protected String league;
    protected Date dateOfBirth;
    protected String ratingMatch1;
    protected String ratingMatch2;
    protected String ratingMatch3;

    public Object parseJsonToObject(JSONObject jsonObject) {

        try {
            this.name = jsonObject.getString("name");
            this.bio = jsonObject.getString("bio");
            this.photoDone = jsonObject.getString("photo done?");
            this.position = jsonObject.getString("position");
            this.specialPlayer = jsonObject.getString("special player? (eg. key player, promising talent, etc)");
            this.playerNumber = jsonObject.getString("number");
            this.caps = jsonObject.getString("caps");
            this.goalsForCountry = jsonObject.getString("goals for country");
            this.club = jsonObject.getString("club");
            this.league = jsonObject.getString("league");

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            try {
                this.dateOfBirth = df.parse(jsonObject.getString("date of birth"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.ratingMatch1 = jsonObject.getString("rating_match1");
            this.ratingMatch2 = jsonObject.getString("rating_match2");
            this.ratingMatch3 = jsonObject.getString("rating_match3");

        }
        catch(JSONException ex){
            System.out.println("Error converting json object to app_objects.Player object!");
            System.out.println(jsonObject.toString(2));
            ex.printStackTrace();
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoDone() {
        return photoDone;
    }

    public String getPosition() {
        return position;
    }

    public String getSpecialPlayer() {
        return specialPlayer;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public String getCaps() {
        return caps;
    }

    public String getGoalsForCountry() {
        return goalsForCountry;
    }

    public String getClub() {
        return club;
    }

    public String getLeague() {
        return league;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRatingMatch1() {
        return ratingMatch1;
    }

    public String getRatingMatch2() {
        return ratingMatch2;
    }

    public String getRatingMatch3() {
        return ratingMatch3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhotoDone(String photoDone) {
        this.photoDone = photoDone;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSpecialPlayer(String specialPlayer) {
        this.specialPlayer = specialPlayer;
    }

    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void setCaps(String caps) {
        this.caps = caps;
    }

    public void setGoalsForCountry(String goalsForCountry) {
        this.goalsForCountry = goalsForCountry;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRatingMatch1(String ratingMatch1) {
        this.ratingMatch1 = ratingMatch1;
    }

    public void setRatingMatch2(String ratingMatch2) {
        this.ratingMatch2 = ratingMatch2;
    }

    public void setRatingMatch3(String ratingMatch3) {
        this.ratingMatch3 = ratingMatch3;
    }
}
