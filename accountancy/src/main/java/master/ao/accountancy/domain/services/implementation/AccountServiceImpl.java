package master.ao.accountancy.domain.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.domain.exceptions.AccountClassNotFoundException;
import master.ao.accountancy.domain.exceptions.AccountNotFoundException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.Account;
import master.ao.accountancy.domain.repositories.AccountRepository;
import master.ao.accountancy.domain.services.AccountService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        validateAccount(account);
        return accountRepository.save(account);
    }


    @Override
    public Account updateAccount(Account account, UUID accountId) {
        return null;
    }

    @Override
    public Optional<Account> fetchOrFail(UUID accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return Optional.of(account);
    }

    @Override
    public List<Account> findAll(Specification<Account> specification) {
        return null;
    }

    private void validateAccount(Account account) {
        var accountDescriptionOptional = accountRepository.findByDescription(account.getDescription());
        if (accountDescriptionOptional.isPresent())
            throw new ExistingDataException("Já existe uma conta registada com esta descrição");

        var accountNumberOptional = accountRepository.findByDescription(account.getDescription());
        if (accountNumberOptional.isPresent())
            throw new ExistingDataException("Já existe uma conta registada com este número");
    }
}
