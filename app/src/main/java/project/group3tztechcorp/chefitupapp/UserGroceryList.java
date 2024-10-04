package project.group3tztechcorp.chefitupapp;

import java.util.ArrayList;

public class UserGroceryList {
    String username;
    ArrayList<String> groceryList;

    public UserGroceryList() {
    }

    public UserGroceryList(String username, ArrayList<String> groceryList) {
        this.username = username;
        this.groceryList = groceryList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(ArrayList<String> groceryList) {
        this.groceryList = groceryList;
    }
}
