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
    @NotEmpty(message = "Vui lòng nhập đầy đủ họ và tên")
    private String name;

    @Email
    @NotEmpty(message = "Vui lòng nhập email")
    private String email;

    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    private String phoneNumber;

    private String address;

    private String age;

    // female: 0 , male: 1
    private String gender;

    @NotEmpty(message = "Vui lòng nhập password")
    private String password;

    @NotEmpty(message = "Vui lòng xác nhận lại password")
    private String confirmPassword;

    private boolean activeStatus;

    private List<String> roles;

}
