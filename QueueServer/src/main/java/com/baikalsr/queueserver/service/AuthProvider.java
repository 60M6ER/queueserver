package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class AuthProvider implements AuthenticationProvider  {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthProvider.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        boolean auth = false;
        String username = authentication.getName().toLowerCase();
        String password = (String) authentication.getCredentials();

        Manager user = (Manager) userService.loadUserByUsername(username);

        if(user != null && (user.getUsername().equals(username) || user.getName().equals(username)))
        {
            if(passwordEncoder.matches(password, user.getPassword()))
            {
                auth = true;
            }
        }

        Authentication authenticate = null;
        if (!auth) {
            ActiveDirectoryLdapAuthenticationProvider adProvider = new ActiveDirectoryLdapAuthenticationProvider("baikal.local", "LDAP://msk-dc01.baikal.local/");
            adProvider.setConvertSubErrorCodesToExceptions(true);
            adProvider.setUseAuthenticationRequestCredentials(true);
            authenticate = adProvider.authenticate(authentication);
            auth = authenticate.isAuthenticated();
        }
        if (auth && user == null && authenticate != null) {
            user = userService.createUserFromAD(username, ((LdapUserDetailsImpl)authenticate.getPrincipal()).getDn());
        }

        if (auth && user != null) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            //if (!securityService.isServiceUser(user))
                //LOGGER.info("Авторизован пользователь: " + user.getName());
            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        }else
            throw new BadCredentialsException("Username not found or Wrong password");
    }

    public boolean matches(CharSequence password, String newPassword){
        return passwordEncoder.matches(password, newPassword);
    }

    public String encodePassword(CharSequence password){
        return passwordEncoder.encode(password);
    }

    public boolean supports(Class<?> arg)
    {
        return true;
    }

}
