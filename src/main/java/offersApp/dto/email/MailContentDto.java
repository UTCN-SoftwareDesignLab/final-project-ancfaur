package offersApp.dto.email;

public class MailContentDto {
    public String username;
    public String email;

    public MailContentDto() {
    }

    public MailContentDto(String username, String email) {
        this.username = username;
        this.email = email;
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
}
