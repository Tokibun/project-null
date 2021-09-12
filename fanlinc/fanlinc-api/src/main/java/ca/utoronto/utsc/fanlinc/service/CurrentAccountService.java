package ca.utoronto.utsc.fanlinc.service;

import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.model.FanlincUserDTO;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service to retrieve the account of the logged in user if they are logged in or null if anonymous user.
 */
@Service
public class CurrentAccountService {

    private AccountRepository accountRepository;

    @Autowired
    public CurrentAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getCurrentAccount() throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof FanlincUserDTO)) {
            throw new AccountNotFoundException();
        }

        // Getting the Account.
        FanlincUserDTO fanlincUserDTO = (FanlincUserDTO) authentication.getPrincipal();
        return accountRepository.findByIdNotDeleted(fanlincUserDTO.getId()).orElseThrow(AccountNotFoundException::new);
    }
}