package LonaShop.service.impl;

import LonaShop.model.Role;
import LonaShop.model.User;
import LonaShop.model.UserDto;
import LonaShop.repository.RoleRepository;
import LonaShop.repository.UserRepository;
import LonaShop.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDto).collect(Collectors.toList());

    }

    @Override
    public UserDto findUserDtoByEmail(String email) {
        User user = findUserByEmail(email);
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAddress(user.getAddress());
        userDto.setId(user.getId());
        userDto.setAge(user.getAge());
        userDto.setGender(user.getGender());
        userDto.setActiveStatus(user.isActiveStatus());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return userDto;
    }

//    private User mapUserDtoToUser(UserDto userDto) {
//        User user = new User();
//        String[] str = user.getName().split(" ");
//        user.setFirstName(str[0]);
//        user.setLastName(str[1]);
//        user.setEmail(user.getEmail());
//        user.setPhoneNumber(user.getPhoneNumber());
//        user.setAddress(user.getAddress());
//        user.setId(user.getId());
//        user.setAge(user.getAge());
//        user.setGender(user.getGender());
//        user.setActiveStatus(user.isActiveStatus());
//        user.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
//        return userDto;
//    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }


}
