package master.ao.accountancy.domain.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.domain.exceptions.AccountNotFoundException;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.exceptions.NatureNotFoundException;
import master.ao.accountancy.domain.models.AccountNature;
import master.ao.accountancy.domain.repositories.AccountNatureRepository;
import master.ao.accountancy.domain.services.CategoryService;
import master.ao.accountancy.domain.services.NatureService;
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
public class NatureServiceImpl implements NatureService {

    private static final String MSG_NATURE_IN_USE = "Natureza não pode ser removida, pois está em uso!";

    private final AccountNatureRepository accountNatureRepository;
    private final CategoryService categoryService;

    @Override
    public AccountNature createAccountNature(AccountNature nature, UUID categoryId) {
        validateNature(nature,categoryId);
        nature.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        nature.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return accountNatureRepository.save(nature);
    }

    @Override
    public AccountNature updateAccountNature(AccountNature nature, UUID natureId) {
        var natureOptional = fetchOrFail(natureId);

        natureOptional.get().setDescription(nature.getDescription());
        natureOptional.get().setCategory(nature.getCategory());

        return accountNatureRepository.save(natureOptional.get());
    }

    @Override
    public Optional<AccountNature> fetchOrFail(UUID natureId) {
        var account = accountNatureRepository.findById(natureId)
                .orElseThrow(() -> new NatureNotFoundException(natureId));

        return Optional.of(account);
    }

    @Override
    public List<AccountNature> findAll(Specification<AccountNature> specification) {
        return accountNatureRepository.findAll(specification);
    }

    @Override
    public void delete(UUID natureId) {
        try {
            var accountOptional = fetchOrFail(natureId).get();
            accountNatureRepository.delete(accountOptional);
            accountNatureRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new AccountNotFoundException(natureId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_NATURE_IN_USE);
        }
    }

    @Override
    public void validateNature(AccountNature nature, UUID categoryId) {

        var categoryOptional = categoryService.fetchOrFail(categoryId);
        nature.setCategory(categoryOptional.get());

        var accountNatureOptional = accountNatureRepository.findByDescription(nature.getDescription());
        if (accountNatureOptional.isPresent())
            throw new ExistingDataException("Já existe uma natureza registada com esta descrição");

    }
}
