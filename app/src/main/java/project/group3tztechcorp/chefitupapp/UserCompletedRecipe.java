package project.group3tztechcorp.chefitupapp;

import java.util.ArrayList;

public class UserCompletedRecipe {
    String username;
    ArrayList<String> completedList;

    public UserCompletedRecipe() {
    }

    public UserCompletedRecipe(String username, ArrayList<String> completedList) {
        this.completedList = completedList;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getCompletedList() {
        return completedList;
    }

    public void setCompletedList(ArrayList<String> completedList) {
        this.completedList = completedList;
    }
}
