package duc.min.controller;

import duc.min.dto.request.SignInForm;
import duc.min.dto.request.SignUpForm;
import duc.min.dto.response.JwtResponse;
import duc.min.dto.response.ResponseMessage;
import duc.min.model.Role;
import duc.min.model.RoleName;
import duc.min.model.User;
import duc.min.security.jwt.JwtProvider;
import duc.min.security.userprincipal.UserPrinciple;
import duc.min.service.role.IRoleService;
import duc.min.service.user.IUSerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUSerService uSerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/signup")
    public ResponseEntity<?>signUp(@RequestBody SignUpForm signUpForm){
        if (uSerService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("username_existed"), HttpStatus.OK);
        }
        if (uSerService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("email_existed"),HttpStatus.OK);
        }
        Set<String>strRoles = signUpForm.getRoles();
        Set<Role>roles = new HashSet<>();
        strRoles.forEach(role->{
            switch (role.toLowerCase()){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(()->new RuntimeException("not_found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow(()->new RuntimeException("not_found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow(()->new RuntimeException("not_found"));
                    roles.add(userRole);

            }
        });
        User user = new User(signUpForm.getName(),signUpForm.getUsername(),signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()),roles);
        uSerService.save(user);
        return new ResponseEntity<>(new ResponseMessage("create_success"),HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?>signIn(@Valid @RequestBody SignInForm signIn){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getUsername(),signIn.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token,userPrinciple.getName(),userPrinciple.getAuthorities()));


    }
}
