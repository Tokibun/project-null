package ca.utoronto.utsc.fanlinc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * Data Class representing an User Account
 */
@Document(collection = "accounts")
public class Account {
    // We're plaintext clowns for the purpose of this course.
    @Id
    private String id;
    private String username, email, password, biography, profileImage;
    private long createdTimestamp, deletedTimestamp; // When the account is deleted, non-zero denotes timestamp when deleted.
    private LocalDate dateOfBirth;
    private Hashtable<String, FandomMembership> fandomMemberships;

    public Account() {
    } // For Unmarshalling

    public Account(String username, String email, String password, String biography, String profileImageURL, long createdTimestamp, long deletedTimestamp, List<FandomMembership> fandomMemberships) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.profileImage = profileImageURL;
        this.createdTimestamp = createdTimestamp;
        this.deletedTimestamp = deletedTimestamp;
        this.fandomMemberships = new Hashtable<>();
    }

    /*
     * Getters/Setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfileImageURL() {
        return profileImage;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImage = profileImageURL;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getDeletedTimestamp() {
        return deletedTimestamp;
    }

    public void setDeletedTimestamp(long deletedTimestamp) {
        this.deletedTimestamp = deletedTimestamp;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Hashtable<String, FandomMembership> getFandomMemberships() {
        return fandomMemberships;
    }

    public String getFandomMembershipType(String fandomId) {
        return fandomMemberships.get(fandomId).type.toString();
    }

    public String getFandomMembershipLevel(String fandomId) {
        return fandomMemberships.get(fandomId).level.toString();
    }

    public void insertFandomMembership(String fandomId, String level, String type) {
        fandomMemberships.put(fandomId, new FandomMembership(level, type));
    }

    public void updateFandomMembershipLevel(String fandomId, String level) {
        FandomMembership fandom = fandomMemberships.get(fandomId);
        fandom.changeFandomLevel(level);
    }

    public void updateFandomMembershipType(String fandomId, String type) {
        FandomMembership fandom = fandomMemberships.get(fandomId);
        fandom.changeFandomType(type);
    }

    public void deleteFandomMembership(String fandomId) {
        fandomMemberships.remove(fandomId);
    }

    public static class FandomMembership {

        public enum FandomLevel {

            LIMITED, CASUAL, INVOLVED, EXPERT

        }

        public enum FandomType {
            FAN, COSPLAYER, VENDOR
        }

        private FandomLevel level;
        private FandomType type;

        private FandomMembership(String level, String type) {
            this.level = FandomLevel.valueOf(level);
            this.type = FandomType.valueOf(type);
        }

        private FandomMembership() {
            // For unwinding
        }

        public void changeFandomLevel(String level) {
            this.level = FandomLevel.valueOf(level);
        }

        public void changeFandomType(String type) {
            this.type = FandomType.valueOf(type);
        }

        public String toString() {
            return level.toString() + " " + type.toString();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return createdTimestamp == account.createdTimestamp &&
                deletedTimestamp == account.deletedTimestamp &&
                Objects.equals(id, account.id) &&
                Objects.equals(username, account.username) &&
                Objects.equals(email, account.email) &&
                Objects.equals(password, account.password) &&
                Objects.equals(biography, account.biography) &&
                Objects.equals(profileImage, account.profileImage) &&
                Objects.equals(dateOfBirth, account.dateOfBirth) &&
                Objects.equals(fandomMemberships, account.fandomMemberships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, biography, profileImage, createdTimestamp, deletedTimestamp, dateOfBirth, fandomMemberships);
    }
}