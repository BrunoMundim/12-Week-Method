package com.mundim.WeekMethod.view;

import com.mundim.WeekMethod.Utils.DateToString;
import com.mundim.WeekMethod.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserView {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String registrationDate;

    public UserView(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.registrationDate = DateToString.localDateToString(user.getRegistrationDate());
    }


}
