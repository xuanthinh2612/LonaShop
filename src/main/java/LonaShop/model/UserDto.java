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
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    @Email
    private String email;
    @NotEmpty(message = "Mật khẩu không được bỏ trống")

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    private String phoneNumber;

    private String password;

    private String address;

    private String age;

    private String gender;

    private boolean activeStatus;

    private List<String> roles;

}
