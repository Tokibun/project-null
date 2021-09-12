package ca.utoronto.utsc.fanlinc.model;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Post {
    @Id
    private String id;
    //author = accountId fandom = fandomId
    private String authorId, message, title, fandomId, creatorLevel, creatorType, creatorUsername;
    private long creationDate;
    private List<Reply> replies;


    public String getCreatorLevel() {
        return creatorLevel;
    }

    public void setCreatorLevel(String creatorLevel) {
        this.creatorLevel = creatorLevel;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public Post(String authorId, String message, String title, long creationDate, String fandomId, String creatorLevel, String creatorType, String creatorUsername) {
        this.authorId = authorId;
        this.message = message;
        this.creationDate = creationDate;
        this.title = title;
        this.fandomId = fandomId;
        this.creatorLevel = creatorLevel;
        this.creatorType = creatorType;
        this.creatorUsername = creatorUsername;
        this.replies = new ArrayList<>();
    }
    public List getList() { return this.replies;}

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

    public String getCreatorUsername() {return creatorUsername;}

    public void addReply(Reply response){
        replies.add(response);
    }
    public void removeReply(String id){
        Iterator a = this.replies.iterator();
        int i = 0;
        Reply comment;
        while(a.hasNext()){
            comment = (Reply) a.next();
            if(comment.getId().equals(id)){
                this.replies.remove(i);
                return;
            }
            i++;
        }

    }
}
