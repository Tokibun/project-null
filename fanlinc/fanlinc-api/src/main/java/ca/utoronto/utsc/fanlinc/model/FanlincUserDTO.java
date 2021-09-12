package ca.utoronto.utsc.fanlinc.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class FanlincUserDTO extends User {

    private String id;
    public FanlincUserDTO(String id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FanlincUserDTO{" +
                "id='" + id + '\'' +
                '}';
    }
}
