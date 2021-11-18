package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Department;
import net.skhu.domain.User;
import net.skhu.model.UserRegistrationModel;
import net.skhu.repository.UserRepository;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean hasErrors(UserRegistrationModel userModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return true;
        if (userModel.getPasswd1().equals(userModel.getPasswd2()) == false) {
            bindingResult.rejectValue("passwd2", null, "비밀번호가 일치하지 않습니다.");
            return true;
        }
        User user = userRepository.findByUserid(userModel.getUserid());
        if (user != null) {
            bindingResult.rejectValue("userid", null, "사용자 아이디가 중복됩니다.");
            return true;
        }
        return false;
    }

    public User createEntity(UserRegistrationModel userModel) {
        User user = new User();
        user.setUserid(userModel.getUserid());
        user.setPassword(userModel.getPasswd1());
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        user.setDepartment(new Department());
        user.getDepartment().setId(userModel.getDepartmentId());
        user.setEnabled(true);
        user.setUserType("학생");
        return user;
    }

    public void save(UserRegistrationModel userModel) {
        User user = createEntity(userModel);
        userRepository.save(user);
    }

}

