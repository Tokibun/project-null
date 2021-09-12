package ca.utoronto.utsc.fanlinc.model;

import org.springframework.data.annotation.Id;

import java.util.*;

/**
 * Data class representing a Fandom
 */
public class Fandom {
    @Id private String id;

    private String displayName, description, bannerImageURL;
    private long creationDate;
    private Hashtable<String, List<String>> user_category_table;

    private Fandom() {
        //For unwinding
    }

    public Fandom(String displayName, String description, String bannerImageURL, long creationDate, String creator,
                  String fandomLvl, String fandomType) {
        this.displayName = displayName;
        this.description = description;
        this.bannerImageURL = bannerImageURL;
        this.creationDate = creationDate;
        user_category_table = new Hashtable<>();
        user_category_table.put("ALL USERS", new ArrayList<>());
        user_category_table.put("LIMITED", new ArrayList<>());
        user_category_table.put("CASUAL", new ArrayList<>());
        user_category_table.put("INVOLVED", new ArrayList<>());
        user_category_table.put("EXPERT", new ArrayList<>());
        user_category_table.put("FAN", new ArrayList<>());
        user_category_table.put("COSPLAYER", new ArrayList<>());
        user_category_table.put("VENDOR", new ArrayList<>());
        user_category_table.get(fandomLvl).add(creator);
        user_category_table.get(fandomType).add(creator);
        user_category_table.get("ALL USERS").add(creator);
    }

    //Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerImageURL() {
        return bannerImageURL;
    }

    public void setBannerImageURL(String bannerImageURL) {
        this.bannerImageURL = bannerImageURL;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getSpecificUserCategoryTable(String key) {
        return user_category_table.get(key);
    }

    public void updateUserinCategoryTable(String userid, String pastCategory, String newCategory) {
        user_category_table.get(pastCategory).remove(userid);
        user_category_table.get(newCategory).add(userid);
    }

    public void addUsertoSpecificCategoryTable(String userId, String category) {
        user_category_table.get(category).add(userId);
    }

    public void removeUserfromCategoryTable(String userId, String categoryLevel, String categoryType) {
        user_category_table.get(categoryLevel).remove(userId);
        user_category_table.get(categoryType).remove(userId);
        user_category_table.get("ALL USERS").remove(userId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fandom fandom = (Fandom) o;
        return creationDate == fandom.creationDate &&
                Objects.equals(id, fandom.id) &&
                Objects.equals(displayName, fandom.displayName) &&
                Objects.equals(description, fandom.description) &&
                Objects.equals(bannerImageURL, fandom.bannerImageURL) &&
                Objects.equals(user_category_table, fandom.user_category_table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, description, bannerImageURL, creationDate, user_category_table);
    }
}
