package project.group3tztechcorp.chefitupapp;

import java.util.ArrayList;

public class UserRewards {

    String username;
    ArrayList<Reward> rewardList;

    public UserRewards() {
    }

    public UserRewards(String username, ArrayList<Reward> rewardList) {
        this.username = username;
        this.rewardList = rewardList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Reward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(ArrayList<Reward> rewardList) {
        this.rewardList = rewardList;
    }
}
