package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserEditDTO {

    private Long id;
    private String username;
    private String birthday;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
}