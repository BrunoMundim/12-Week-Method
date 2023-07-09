package com.mundim.WeekMethod.security;

import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.UnauthorizedRequestException;
import com.mundim.WeekMethod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.UNAUTHORIZED_USER;

@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email);
    }

    public void verifyUserAuthentication(User user) {
        User loggedUser = findUserByBearer();
        if(!loggedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && !loggedUser.equals(user)){
            throw new UnauthorizedRequestException(UNAUTHORIZED_USER.getMessage());
        }
    }

    public User findUserByBearer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findUserByEmail(currentPrincipalName);
    }
}