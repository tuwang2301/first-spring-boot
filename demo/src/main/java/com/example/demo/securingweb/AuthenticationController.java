package com.example.demo.securingweb;

import com.example.demo.common.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO){
        try{
            ApplicationUser newUser = authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword());
            return ResponseEntity.ok(new ResponseObject<>("success","Register successfully",newUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",e.getMessage(),"Register fail"));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody RegistrationDTO body){
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(body.getUsername(), body.getPassword());
        if(loginResponseDTO.getUser()==null){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail","Login fail",null));
        }else{
            return ResponseEntity.ok(new ResponseObject<>("success","Login successfully",loginResponseDTO));
        }
    }
}
