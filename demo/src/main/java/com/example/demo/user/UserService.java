package com.example.demo.user;

import com.example.demo.enumUsages.RoleName;
import com.example.demo.securingweb.Role;
import com.example.demo.securingweb.RoleRepository;
import com.example.demo.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

    public Page<UserProjection> getAllUsers(String username, Integer currentPage, Integer pageSize) {
        Specification spec = Specification.where(null);

        if(username != null){
            spec = spec.and(UserSpecification.hasUserName(username));
        }

        Page<UserProjection> page = userRepository.findAll(spec, PageRequest.of(currentPage-1,pageSize));
        return page;
    }

    public ApplicationUser addNewUser(ApplicationUserDTO newUser) {
        try{
            ApplicationUser user = new ApplicationUser();
            if(userRepository.findByUsername(newUser.getUsername()).isPresent()){
                throw new UserException(UserErrors.Username_Exist);
            }
            user.setUsername(newUser.getUsername());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            Set<Role> roles = new HashSet<>();
            for (String roleName: newUser.getRoles()) {
                RoleName roleName1 = Validate.validateRoleName(roleName);
                Role role = roleRepository.findByAuthority(roleName1).get();
                roles.add(role);
            }
            user.setAuthorities(roles);
            userRepository.save(user);
            return user;
        }catch (Exception e){
            throw e;
        }
    }
}
