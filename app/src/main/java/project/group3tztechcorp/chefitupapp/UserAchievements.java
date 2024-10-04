package project.group3tztechcorp.chefitupapp;

import java.util.ArrayList;

public class UserAchievements {
    String username;
    ArrayList<String> achievementsList;

    public UserAchievements() {
    }

    public UserAchievements(String username, ArrayList<String> achievementsList) {
        this.username = username;
        this.achievementsList = achievementsList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getAchievementsList() {
        return achievementsList;
    }

    public void setAchievementsList(ArrayList<String> achievementsList) {
        this.achievementsList = achievementsList;
    }
}
