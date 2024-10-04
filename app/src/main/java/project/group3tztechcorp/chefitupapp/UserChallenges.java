package project.group3tztechcorp.chefitupapp;

import java.util.ArrayList;

public class UserChallenges {

    String username;
    ArrayList<String> challengeList;

    public UserChallenges() {
    }

    public UserChallenges(String username, ArrayList<String> challengeList) {
        this.username = username;
        this.challengeList = challengeList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(ArrayList<String> challengeList) {
        this.challengeList = challengeList;
    }
}
