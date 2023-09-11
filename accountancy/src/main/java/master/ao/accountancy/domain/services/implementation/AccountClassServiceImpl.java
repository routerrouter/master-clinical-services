package master.ao.accountancy.domain.services.implementation;

import lombok.AllArgsConstructor;
import master.ao.accountancy.domain.exceptions.AccountClassNotFoundException;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.Account;
import master.ao.accountancy.domain.models.AccountClass;
import master.ao.accountancy.domain.repositories.AccountClassRepository;
import master.ao.accountancy.domain.services.AccountClassService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountClassServiceImpl implements AccountClassService {

    private static final String MSG_ACCOUNT_CLASS_IN_USE
            = "Classe de conta não pode ser removido, pois está em uso!";

    private final AccountClassRepository accountClassRepository;

    @Override
    public AccountClass createAccountClass(AccountClass accountClass) {
        var accountClassOptional = accountClassRepository.findByDescription(accountClass.getDescription());
        if (accountClassOptional.isPresent())
            throw new ExistingDataException("Já existe uma classe de contas com esta descrição");

        accountClass.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        accountClass.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return accountClassRepository.save(accountClass);
    }

    @Override
    public AccountClass updateAccountClass(UUID accountClassId, AccountClass accountClass) {
        var accountClassOptional = fetchOrFail(accountClassId);
        accountClassOptional.get().setDescription(accountClass.getDescription());
        accountClassOptional.get().setNumber(accountClass.getNumber());
        return accountClassRepository.save(accountClassOptional.get());
    }

    @Override
    public Optional<AccountClass> fetchOrFail(UUID accountClassId) {
        var accountClass = accountClassRepository.findById(accountClassId)
                .orElseThrow(() -> new AccountClassNotFoundException(accountClassId));

        return Optional.of(accountClass);
    }

    @Override
    public List<AccountClass> findAllAccountClass(Specification<AccountClass> specification) {
        return accountClassRepository.findAll(specification);
    }

    @Override
    public void delete(UUID accountClassId) {
        try {
            var categoryOptional = fetchOrFail(accountClassId).get();
            accountClassRepository.delete(categoryOptional);
            accountClassRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new AccountClassNotFoundException(accountClassId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_ACCOUNT_CLASS_IN_USE);
        }
    }
}
