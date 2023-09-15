package master.ao.accountancy.domain.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.domain.exceptions.AccountNotFoundException;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.Account;
import master.ao.accountancy.domain.repositories.AccountRepository;
import master.ao.accountancy.domain.services.AccountClassService;
import master.ao.accountancy.domain.services.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private static final String MSG_ACCOUNT_IN_USE = "Conta não pode ser removida, pois está em uso!";

    private final AccountRepository accountRepository;
    private final AccountClassService accountClassService;

    @Override
    public Account createAccount(Account account, UUID accountClassId) {
        validateAccount(account,accountClassId);
        account.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        account.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account, UUID accountId) {
        var accountOptional = fetchOrFail(accountId);

        accountOptional.get().setDescription(account.getDescription());
        accountOptional.get().setNumber(account.getNumber());
        accountOptional.get().setAccountClass(account.getAccountClass());
        accountOptional.get().setAccountType(account.getAccountType());

        return accountRepository.save(accountOptional.get());
    }

    @Override
    public Optional<Account> fetchOrFail(UUID accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return Optional.of(account);
    }

    @Override
    public List<Account> findAll(Specification<Account> specification) {
        return accountRepository.findAll(specification);
    }

    @Override
    public void delete(UUID accountId) {
        try {
            var accountOptional = fetchOrFail(accountId).get();
            accountRepository.delete(accountOptional);
            accountRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new AccountNotFoundException(accountId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_ACCOUNT_IN_USE);
        }
    }

    @Override
    public void validateAccount(Account account, UUID classId) {

        var accounClass = accountClassService.fetchOrFail(classId);
        account.setAccountClass(accounClass.get());

        var accountDescriptionOptional = accountRepository.findByDescription(account.getDescription());
        if (accountDescriptionOptional.isPresent())
            throw new ExistingDataException("Já existe uma conta registada com esta descrição");

        var accountNumberOptional = accountRepository.findByNumber(account.getNumber());
        if (accountNumberOptional.isPresent())
            throw new ExistingDataException("Já existe uma conta registada com este número");
    }
}
