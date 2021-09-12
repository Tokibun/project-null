package ca.utoronto.utsc.fanlinc.model;

import org.springframework.data.annotation.Id;

public class Reply {
    @Id private String id;

    private String authorId, message, title, fandomId, creatorLevel, creatorType, creatorUsername;
    private long creationDate;
    public Reply(String authorId, String message, long creationDate, String fandomId, String creatorLevel, String creatorType, String creatorUsername) {
        this.authorId = authorId;
        this.message = message;
        this.creationDate = creationDate;
        this.fandomId = fandomId;
        this.creatorLevel = creatorLevel;
        this.creatorType = creatorType;
        this.creatorUsername = creatorUsername;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public String getFandomId() {
        return fandomId;
    }

    public void setFandomId(String fandomId) {
        this.fandomId = fandomId;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
