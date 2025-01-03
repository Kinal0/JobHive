package com.franklin.jobhive.secure;

import com.franklin.jobhive.secure.role.Role;
import com.franklin.jobhive.secure.role.RoleRepository;
import com.franklin.jobhive.secure.user.CustomUserDetails;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.secure.user.UserDTO;
import com.franklin.jobhive.secure.user.UserRepository;
import com.franklin.jobhive.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SkillService skillService;

    @Autowired
    List<String> securityQuestions;

    @GetMapping("/")
    public String Home_Page(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        model.addAttribute("username", user.getFirst_name());

        return "welcome_page";
    }


    @GetMapping("/login")
    public String Login()
    {
        return "jobhive_login";
    }



    @GetMapping("/error")
    public String handleError() {
        return "error";
    }


    @GetMapping("user/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("skilllist", skillService.readSkills());
        model.addAttribute("securityQuestion", securityQuestions );

        return "register_user";
    }

    @PostMapping("user/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {

        User existingUser1 = userRepository.getUserByUsername(userDTO.getUser_name());
        User existingUser2 = userRepository.getUserByEmail(userDTO.getEmail());
        if(existingUser1!=null) {
            model.addAttribute("errorMessage", "User name already taken!");
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("skilllist", skillService.readSkills());
            model.addAttribute("securityQuestion", securityQuestions );
            return "register_user";
        }

        else if(existingUser2!=null){
            model.addAttribute("errorMessage", "User with entered email already exists!");
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("skilllist", skillService.readSkills());
            model.addAttribute("securityQuestion", securityQuestions );
            return "register_user";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role role = roleRepository.findById(userDTO.getRole_id()).get();
        User user = new User();
        user.setUser_name(userDTO.getUser_name());
        user.setFirst_name(userDTO.getFirst_name());
        user.setLast_name(userDTO.getLast_name());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setSecurityQuestion1(userDTO.getSecurityQuestion1());
        user.setSecurityAnswer1(userDTO.getSecurityAnswer1().toLowerCase());
        user.setSecurityQuestion2(userDTO.getSecurityQuestion2());
        user.setSecurityAnswer2(userDTO.getSecurityAnswer2().toLowerCase());
        user.setSkills(userDTO.getAllSkills());
        user.getRoles().add(role);
        model.addAttribute("errorMessage", null);
        model.addAttribute("successMessage", "User Successfully Created!");
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("skilllist", skillService.readSkills());
        model.addAttribute("securityQuestion", securityQuestions );
        userRepository.save(user);
        return "register_user";
    }

    @GetMapping("user/finduser")
    public String findUser() {
        return "find_user";
    }

    @PostMapping("user/forgotpassword")
    public String showForgotPassword(@RequestParam String searchterm, Model model) {

        System.out.println(searchterm);

        User user1 = userRepository.getUserByEmail(searchterm);
        User user2 = userRepository.getUserByUsername(searchterm);

        if(user1!=null){


            model.addAttribute("username", user1.getUser_name());
            model.addAttribute("securityQuestion1", user1.getSecurityQuestion1());
            model.addAttribute("securityQuestion2", user1.getSecurityQuestion2());

            return "forgotpassword_user";
        }
        else if(user2!=null){

            model.addAttribute("username", user2.getUser_name());
            model.addAttribute("securityQuestion1", user2.getSecurityQuestion1());
            model.addAttribute("securityQuestion2", user2.getSecurityQuestion2());
            return "forgotpassword_user";
        }

        else{
            model.addAttribute("errorMessage", "Unable to find user with provided username or email");
            return "find_user";
        }

    }

    @PostMapping("user/passwordreset")
    public String passwordReset(@RequestParam String username,  String securityAnswer1, String securityAnswer2, String newpassword, String confirmpassword, Model model){


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.getUserByUsername(username);

        if(newpassword.equals(confirmpassword) && securityAnswer1.toLowerCase().equals(user.getSecurityAnswer1()) && securityAnswer2.toLowerCase().equals(user.getSecurityAnswer2())) {
            String encodedPassword = encoder.encode(newpassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
        else if(!securityAnswer1.toLowerCase().equals(user.getSecurityAnswer1())){
            model.addAttribute("username", username);
            model.addAttribute("securityQuestion1", user.getSecurityQuestion1());
            model.addAttribute("securityQuestion2", user.getSecurityQuestion2());
            model.addAttribute("errorMessage2", "");
            model.addAttribute("errorMessage1", "Incorrect Answer");
            return "forgotpassword_user";
        }

        else if(!securityAnswer2.toLowerCase().equals(user.getSecurityAnswer2())){
            model.addAttribute("username", username);
            model.addAttribute("securityQuestion1", user.getSecurityQuestion1());
            model.addAttribute("securityQuestion2", user.getSecurityQuestion2());
            model.addAttribute("errorMessage1", "");
            model.addAttribute("errorMessage2", "Incorrect Answer");
            return "forgotpassword_user";
        }


        model.addAttribute("success", "Password reset successful for "+username);
        return "find_user";
    }

    @GetMapping("user/edit")
    public String showEditForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_name(user.getUser_name());
        userDTO.setFirst_name(user.getFirst_name());
        userDTO.setLast_name(user.getLast_name());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setSecurityQuestion1(user.getSecurityQuestion1());
        userDTO.setSecurityQuestion2(user.getSecurityQuestion2());
        userDTO.setSecurityAnswer1(user.getSecurityAnswer1());
        userDTO.setSecurityAnswer2(user.getSecurityAnswer2());
        userDTO.setSkills(user.getSkills().split("#"));
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("skilllist", skillService.readSkills());
        model.addAttribute("securityQuestion", securityQuestions );

        return "edit_user";
    }

    @PostMapping("user/save")
    public String saveUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);

        if(!user.getUser_name().equals(userDTO.getUser_name())) {
            model.addAttribute("errorMessage", "User name already taken!");
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("skilllist", skillService.readSkills());
            model.addAttribute("securityQuestion", securityQuestions );
            return "edit_user";
        }

        else if(!user.getEmail().equals(userDTO.getEmail())){
            model.addAttribute("errorMessage", "User with entered email already exists!");
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("skilllist", skillService.readSkills());
            model.addAttribute("securityQuestion", securityQuestions );
            return "edit_user";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.encode(userDTO.getPassword()).equals(user.getPassword())){
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }


        user.setUser_name(userDTO.getUser_name());
        user.setFirst_name(userDTO.getFirst_name());
        user.setLast_name(userDTO.getLast_name());
        user.setEmail(userDTO.getEmail());
        user.setSecurityQuestion1(userDTO.getSecurityQuestion1());
        user.setSecurityAnswer1(userDTO.getSecurityAnswer1().toLowerCase());
        user.setSecurityQuestion2(userDTO.getSecurityQuestion2());
        user.setSecurityAnswer2(userDTO.getSecurityAnswer2().toLowerCase());
        user.setSkills(userDTO.getAllSkills());
        model.addAttribute("errorMessage", "");
        model.addAttribute("successMessage", "User Successfully Created!");
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("skilllist", skillService.readSkills());
        model.addAttribute("securityQuestion", securityQuestions );
        userRepository.save(user);
        return "redirect:/";
    }

}

