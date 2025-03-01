package LonaShop.service;

import LonaShop.model.User;
import LonaShop.model.UserDto;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);
    User findUserById(Long id);
    User findUserByEmail(String email);
    User findUserByPhoneNumber(String phoneNumber);
    UserDto findUserDtoByEmail(String email);

    List<UserDto> findAllUsers();

}
