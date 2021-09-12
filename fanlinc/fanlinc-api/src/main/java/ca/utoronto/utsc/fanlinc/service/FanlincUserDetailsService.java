package ca.utoronto.utsc.fanlinc.service;

import ca.utoronto.utsc.fanlinc.model.FanlincUserDTO;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Loads user details from Mongo as a DAO.
 */
@Service
public class FanlincUserDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public FanlincUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Finds the user by the username and returns a Spring User object which the username and password of the user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found.", username)));
        if (account.getDeletedTimestamp() != 0)
            throw new UsernameNotFoundException(String.format("Username %s not found.", username)); // Deleted accounts can't sign in.
        // {noop} is needed for plaintext passwords which is okay for the purposes of this course.
        return new FanlincUserDTO(account.getId(), account.getUsername(), "{noop}" + account.getPassword(), Collections.emptyList());
    }
}
