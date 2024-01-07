package LonaShop.service;

import LonaShop.model.User;
import LonaShop.model.UserDto;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
    UserDto findUserDtoByEmail(String email);

    List<UserDto> findAllUsers();

}
