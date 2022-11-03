package duc.min.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SignInForm {
    private String username;
    private String password;

    public SignInForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignInForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
