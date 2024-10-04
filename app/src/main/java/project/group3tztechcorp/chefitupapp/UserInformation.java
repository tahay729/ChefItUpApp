package project.group3tztechcorp.chefitupapp;

public class UserInformation {
    String username, fullName;
    int level, experience, rewards, recipesCompleted, achievementsCompleted;

    public UserInformation() {
    }

    public UserInformation(String username, String fullName, int level, int experience, int rewards, int recipesCompleted, int achievementsCompleted) {
        this.username = username;
        this.fullName = fullName;
        this.level = level;
        this.experience = experience;
        this.rewards = rewards;
        this.recipesCompleted = recipesCompleted;
        this.achievementsCompleted = achievementsCompleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public int getRecipesCompleted() {
        return recipesCompleted;
    }

    public void setRecipesCompleted(int recipesCompleted) {
        this.recipesCompleted = recipesCompleted;
    }

    public int getAchievementsCompleted() {
        return achievementsCompleted;
    }

    public void setAchievementsCompleted(int achievementsCompleted) {
        this.achievementsCompleted = achievementsCompleted;
    }

    public void addCompletedRecipes(){
        this.recipesCompleted += 1;
    }

    public void addAchievementsCompleted(){
        this.achievementsCompleted += 1;
    }

    public void addRewards(){
        this.rewards += 1;
    }

    public void resetRewards(){
        this.rewards = 0;
    }

    public void checkLevel(){
        if (this.experience <= 300){
            this.level = 1;
        } else if (this.experience > 300 && this.experience <= 701){
            this.level = 2;
        } else if (this.experience > 701 && this.experience <= 1301){
            this.level = 3;
        } else if (this.experience > 1301 && this.experience <= 2101){
            this.level = 4;
        } else if (this.experience > 2100){
            this.level = 5;
        }
    }

    public void increaseExp(String level){
        if(level.equals("Easy")) {
            this.experience += 50;
        } else if(level.equals("Intermediate")){
            this.experience += 100;
        } else if(level.equals("Hard")){
            this.experience += 150;
        } else {
            this.experience += 20;
        }
    }

    public void increaseExpChallenge(String level){
        if(level.equals("Easy")) {
            this.experience += 100;
        } else if(level.equals("Intermediate")){
            this.experience += 200;
        } else if(level.equals("Hard")){
            this.experience += 300;
        } else {
            this.experience += 40;
        }
    }
}
