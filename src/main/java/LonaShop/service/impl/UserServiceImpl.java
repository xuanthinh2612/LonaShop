package LonaShop.service.impl;

import LonaShop.model.Role;
import LonaShop.model.User;
import LonaShop.model.UserDto;
import LonaShop.repository.RoleRepository;
import LonaShop.repository.UserRepository;
import LonaShop.service.UserService;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl { //implements UserService {

    private final String ROLE_ADMIN = "ROLE_ADMIN";

//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;

//    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


//    @Override
    public void saveUser(UserDto userDto) {
//        User user = new User();
//        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
//        user.setEmail(userDto.getEmail());
//        // encrypt the password using spring security
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//        Role role = roleRepository.findByName("ROLE_USER");
//        if ( role == null) {
//            role = checkRoleExist();
//        }
//        user.setRoles(Arrays.asList(role));
//        userRepository.save(user);

    }

//    @Override
//    public User findUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    @Override
//    public List<UserDto> findAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
//
//    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
//        String[] str = user.getName().split(" ");
//        userDto.setFirstName(str[0]);
//        userDto.setLastName(str[1]);
//        userDto.setEmail(user.getEmail());
//        userDto.setPhoneNumber(user.getPhoneNumber());
//        userDto.setAddress(user.getAddress());
//        userDto.setId(user.getId());
//        userDto.setAge(user.getAge());
//        userDto.setGender(user.getGender());
//        userDto.setActiveStatus(user.isActiveStatus());
//        userDto.setRoles(user.getRoles().stream().map((role) -> role.getName()).collect(Collectors.toList()));
        return userDto;
    }

//    private Role checkRoleExist() {
//        Role role = new Role();
//        role.setName("ROLE_USER");
//        return roleRepository.save(role);
//    }


}
