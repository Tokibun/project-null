package ca.utoronto.utsc.fanlinc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ca.utoronto.utsc.fanlinc.model.Account;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface AccountRepository extends MongoRepository<Account, String> {

    /**
     * findById and returns only if account is not deleted.
     */
    default Optional<Account> findByIdNotDeleted(String id) {
        AtomicReference<Optional<Account>> account = new AtomicReference<>(findById(id));
        account.get().ifPresent(a -> {
            if (a.getDeletedTimestamp() != 0) {
                account.set(Optional.empty());
            }
        });
        return account.get();
    }

    default Optional<Account> findByUsernameNotDeleted(String username) {
        AtomicReference<Optional<Account>> account = new AtomicReference<>(findAccountByUsername(username));
        account.get().ifPresent(a -> {
            if (a.getDeletedTimestamp() != 0) {
                account.set(Optional.empty());
            }
        });
        return account.get();
    }

    Optional<Account> findAccountByUsername(String username);

    Optional<Account> findByEmail(String email);

    Optional<Account> findAccountByUsernameOrEmail(String username, String email);
}