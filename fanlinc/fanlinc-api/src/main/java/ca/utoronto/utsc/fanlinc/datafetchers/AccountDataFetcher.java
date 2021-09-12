package ca.utoronto.utsc.fanlinc.datafetchers;

import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.model.Fandom;
import ca.utoronto.utsc.fanlinc.model.FandomMembershipEntry;
import ca.utoronto.utsc.fanlinc.repository.FandomRepository;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import ca.utoronto.utsc.fanlinc.exceptions.IllegalAccountModificationException;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * All the data fetchers that relate to account stuff
 */
@Service
public class AccountDataFetcher {

    private AccountRepository accountRepository;
    private FandomRepository fandomRepository;
    private CurrentAccountService currentAccountService;

    @Autowired
    public AccountDataFetcher(AccountRepository accountRepository, FandomRepository fandomRepository, CurrentAccountService currentAccountService) {
        this.accountRepository = accountRepository;
        this.currentAccountService = currentAccountService;
        this.fandomRepository = fandomRepository;
    }

    /*
     * Query
     */

    /**
     * Returns the current account which is logged in and null if not found.
     * <p>
     * Schema: currentAccount
     */
    public DataFetcher currentAccount() {
        return env -> {
            try {
                return currentAccountService.getCurrentAccount();
            } catch (AccountNotFoundException e) {
                return null;
            }
        };
    }

    /**
     * Returns the account with the associated id or null if not found
     * <p>
     * Schema: accountById
     */
    public DataFetcher accountById() {
        return env -> {
            String accountId = env.getArgument("id");
            return accountRepository.findById(accountId).orElse(null);
        };
    }

    /**
     * Return the account with the assocaited username or null if not found
     * <p>
     * Schema: accountByUsername
     */
    public DataFetcher accountByUsername() {
        return env -> {
            String username = env.getArgument("username");
            return accountRepository.findAccountByUsername(username).orElse(null);
        };
    }

    /*
     * Mutation
     */

    /**
     * Create an account with the specified information.
     * Note that dateOfBirth is in ISO-8601 and mutation returns null if an account with the username/email exists.
     * <p>
     * Schema: createAccount
     */
    public DataFetcher createAccount() {
        return env -> {
            String username = env.getArgument("username");
            String email = env.getArgument("email");
            String password = env.getArgument("password");
            String biography = env.getArgument("biography");
            String profileImage = env.getArgument("profileImage");

            // Account with the same username or email exists
            if (accountRepository.findAccountByUsernameOrEmail(username, email).isPresent())
                throw new IllegalAccountModificationException();

            return accountRepository.insert(new Account(username, email, password, biography, profileImage,
                    System.currentTimeMillis(), 0, new ArrayList<>()));
        };
    }

    /**
     * Update account with the specified email, password, biography and profile image and returns the modified account upon success.
     * If the accountId is specified, it uses that, else it tries to use the logged in user.
     * <p>
     * Schema: updateAccount, updateAccountById
     */
    public DataFetcher updateAccount() {
        return env -> {
            // Get account
            Account account = currentAccountService.getCurrentAccount();

            if (account == null) throw new AccountNotFoundException();

            // can you make it so if its empty string nothing happns? for email & password
            if (env.containsArgument("email")) {
                String email = env.getArgument("email");
                if(!email.equals("")) {
                    if (accountRepository.findByEmail(email).orElse(null) == null) {
                        throw new IllegalAccountModificationException();
                    }
                    account.setEmail(email);
                }
            }

            if (env.containsArgument("password")) {
                String password = env.getArgument("password");
                if(!password.equals("")) account.setPassword(password);
            }

            if (env.containsArgument("biography")) {
                account.setBiography(env.getArgument("biography"));
            }

            if (env.containsArgument("profileImage")) {
                account.setProfileImageURL(env.getArgument("profileImage"));
            }

            return accountRepository.save(account);
        };
    }

    /**""
     * Soft deletes the specified account by setting the deletedTimestamp to current epoch time. Fails if account not
     * found.
     * <p>
     * Schema: deleteAccount, deleteAccountById
     */
    public DataFetcher deleteAccount() {
        return env -> {
            Account account;

            if (env.containsArgument("accountId")) {
                String accountId = env.getArgument("accountId");
                account = accountRepository.findByIdNotDeleted(accountId).orElse(null);
            } else {
                account = currentAccountService.getCurrentAccount();
            }

            if (account == null)
                throw new AccountNotFoundException();

            account.setDeletedTimestamp(System.currentTimeMillis());
            accountRepository.save(account);
            return true;
        };
    }

    /**
     * Iterates through all accounts to find accounts with substring within username
     *
     * @return List of Accounts with substring as part of username
     */
    public DataFetcher accountsByUsernameSubstring() {
        return env -> {
            String substring = env.getArgument("accountUsernameSubstring");
            if (substring.equals("")) {
                return new ArrayList<Account>();
            } else {
                //Gets all fandoms, puts relevant ones into arrayList -> returned as array
                List<Account> accountList = new ArrayList<>();
                Iterator i = accountRepository.findAll().iterator();
                while (i.hasNext()) {
                    Account account = (Account) i.next();
                    if (account.getUsername().contains(substring)) {
                        accountList.add(account);
                    }
                }
                return accountList;
            }
        };
    }

    /**
     * Returns list of objects containing fandom ids, level, and type, for a specific user's fandom membership
     * This is needed in order to display a user's fandoms in profile page
     */
    public DataFetcher membershipsByUsername() {
        return env -> {
            String username = env.getArgument("username");
            Account account = accountRepository.findAccountByUsername(username).orElse(null);
            if (account == null) {
                return null;
            } else {
                List<FandomMembershipEntry> membershipList = new ArrayList<>();
                Iterator it = account.getFandomMemberships().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String id = (String) entry.getKey();
                    Fandom fandom = fandomRepository.findById(id).orElse(null);
                    String level = entry.getValue().toString().split(" ")[0];
                    String type = entry.getValue().toString().split(" ")[1];
                    //Create fandomMembershipEntry object
                    FandomMembershipEntry membership = new FandomMembershipEntry(fandom, level, type);
                    membershipList.add(membership);
                }
                return membershipList;
            }
        };
    }
}
