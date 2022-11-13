package gr.thegoodsideofe1.tourguide.controllers;

import gr.thegoodsideofe1.tourguide.aes.AES_ENCRYPTION;
import gr.thegoodsideofe1.tourguide.entities.User;
import gr.thegoodsideofe1.tourguide.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.util.*;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AES_ENCRYPTION aes_encryption;

    @Transactional
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> list(){
        return userService.listAllUsers();
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> get (@PathVariable Integer id){
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer id){
        try {
            User existUser = userService.getUser(id);
            user.setId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
            User updatedUser = userService.getUser(id);
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        User deletedUser = userService.getUser(id);
        userService.deleteUser(id);
        return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/login/email", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody Map<String, String> userLoginDetails) throws Exception {
        Map<String, String> returnResponse = new HashMap<>();
        String emailParam = userLoginDetails.get("email");
        String passwordParam = userLoginDetails.get("password");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        User userToLogin = userService.getUserByEmail(emailParam);

        if (userToLogin == null){
            //Email Does not Exists
            returnResponse.put("status", "error");
            returnResponse.put("message", "Email or Password is incorrect");
            return returnResponse;
        }
        //User Exists
        if (passwordEncoder.matches(passwordParam, userToLogin.getPassword())){
            //Password Param matches password in DB
            String userDetailsJoined = userToLogin.getEmail() + "," + userToLogin.getUsername() + "," + userToLogin.getFirstName() + "," + userToLogin.getLastName();

            returnResponse.put("status", "success");
            returnResponse.put("token", aes_encryption.encrypt(userDetailsJoined));
        }
        returnResponse.put("test", String.valueOf(passwordEncoder.matches(passwordParam, userToLogin.getPassword())));
        return returnResponse;
    }

    @RequestMapping(value = "/login/username", method = RequestMethod.POST)
    public Map<String, String> loginUsername(@RequestBody Map<String, String> userLoginDetails) throws Exception {
        Map<String, String> returnResponse = new HashMap<>();
        String usernameParam = userLoginDetails.get("username");
        String passwordParam = userLoginDetails.get("password");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        User userToLogin = userService.getUserByUsername(usernameParam);

        if (userToLogin == null){
            //Email Does not Exists
            returnResponse.put("status", "error");
            returnResponse.put("message", "Username or Password is incorrect");
            return returnResponse;
        }

        if (passwordEncoder.matches(passwordParam, userToLogin.getPassword())){
            //Password Param matches password in DB
            String userDetailsJoined = userToLogin.getEmail() + "," + userToLogin.getUsername() + "," + userToLogin.getFirstName() + "," + userToLogin.getLastName();

            returnResponse.put("status", "success");
            returnResponse.put("token", aes_encryption.encrypt(userDetailsJoined));
        }
        returnResponse.put("test", String.valueOf(passwordEncoder.matches(passwordParam, userToLogin.getPassword())));
        return returnResponse;
    }
}