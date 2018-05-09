package offersApp.entity.builder;

import offersApp.entity.Role;
import offersApp.entity.User;

import java.util.List;

public class UserBuilder {
    private User user;
    public UserBuilder(){
        user = new User();
    }

    public UserBuilder setId(Long id){
        user.setId(id);
        return this;
    }

    public UserBuilder setUsername(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder setPassword(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder setEmail(String email){
        user.setEmail(email);
        return this;
    }

    public UserBuilder setRoles(List<Role> roles){
        user.setRoles(roles);
        return this;
    }

    public User build(){
        return user;
    }

}