package LonaShop.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotEmpty(message = "Vui lòng nhập họ")
    private String firstName;
    @NotEmpty(message = "Vui lòng tên")
    private String lastName;

    @Email
    @NotEmpty(message = "Vui lòng nhập email")
    private String email;

    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    private String phoneNumber;

    @NotEmpty(message = "Vui lòng nhập password")
    private String password;

    private String address;

    private String age;

    private String gender;

    private boolean activeStatus;

    private List<String> roles;

}
