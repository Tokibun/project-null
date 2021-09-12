package ca.utoronto.utsc.fanlinc.model;

import org.springframework.data.annotation.Id;

/**
 * Part of the solution to get user's fandoms
 * Since we're storing memberships in a hashtable, we can't just select for the fandom field.
 * This is a roundabout attempt to solve that issue. The schema contains a query that returns a list of this object.
 */
public class FandomMembershipEntry{
    @Id
    private String level;
    private String type;
    private Fandom fandom;

    public FandomMembershipEntry(Fandom fandom, String level, String type){
        this.level = level;
        this.type = type;
        this.fandom = fandom;
    }
}
