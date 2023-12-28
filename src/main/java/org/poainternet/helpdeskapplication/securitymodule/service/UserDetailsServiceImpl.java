package org.poainternet.helpdeskapplication.securitymodule.service;

import org.poainternet.helpdeskapplication.securitymodule.definitions.UserDetailsImpl;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("Account with username " + username + " not found"));

        return new UserDetailsImpl(userAccount);
    }
}
