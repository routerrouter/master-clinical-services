package master.ao.accountancy.domain.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.responses.NatureDescriptionResponse;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.exceptions.SubAccountNotFoundException;
import master.ao.accountancy.domain.models.ProviderNature;
import master.ao.accountancy.domain.models.SubAccount;
import master.ao.accountancy.domain.repositories.ProviderNatureRepository;
import master.ao.accountancy.domain.repositories.SubAccountRepository;
import master.ao.accountancy.domain.services.AccountService;
import master.ao.accountancy.domain.services.NatureService;
import master.ao.accountancy.domain.services.SubAccountService;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
import master.ao.accountancy.domain.utilities.Constants;
import master.ao.accountancy.domain.utilities.Converts;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class SubAccountServiceImpl implements SubAccountService {

    private static final String MSG_SUBACCOUNT_IN_USE = "Subconta não pode ser removida, pois está em uso!";

    private final SubAccountRepository subAccountRepository;
    private final ProviderNatureRepository providerNatureRepository;
    private final AccountService accountService;
    private final NatureService natureService;
    private final Converts convert;
    private final Constants constants;

    @Override
    public SubAccount createSubAccount(SubAccount subAccount, UUID accountId) {
        validateSubAccount(subAccount, accountId);
        subAccount.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        subAccount.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return subAccountRepository.save(subAccount);
    }

    @Override
    public SubAccount updateSubAccount(SubAccount account, UUID accountId) {
        var subAccountOptional = fetchOrFail(accountId);

        subAccountOptional.get().setNif(account.getNif());
        subAccountOptional.get().setDescription(account.getDescription());
        subAccountOptional.get().setAccount(account.getAccount());
        subAccountOptional.get().setMovement(account.getMovement());
        subAccountOptional.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return subAccountRepository.save(subAccountOptional.get());
    }

    @Override
    public Optional<SubAccount> fetchOrFail(UUID subAccountId) {
        var account = subAccountRepository.findById(subAccountId)
                .orElseThrow(() -> new SubAccountNotFoundException(subAccountId));

        return Optional.of(account);
    }

    @Override
    public List<SubAccount> findAll(Specification<SubAccount> specification, UUID accountId) {

        if (accountId != null) {
            return subAccountRepository.findAll(specification)
                    .stream()
                    .filter(subAccount -> convert.convertUuidToString(subAccount.getAccount().getAccountId())
                            .equals(convert.convertUuidToString(accountId)))
                    .sorted((o1, o2) -> o1.getNumber().
                            compareTo(o2.getNumber()))
                    .collect(Collectors.toList());
        }
        return subAccountRepository.findAll(specification);
    }


    @Override
    public void delete(UUID subAccountId) {
        try {
            var subAccountOptional = fetchOrFail(subAccountId).get();
            subAccountRepository.delete(subAccountOptional);
            subAccountRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new SubAccountNotFoundException(subAccountId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_SUBACCOUNT_IN_USE);
        }
    }

    @Override
    public List<SubAccount> findAllProviders(SpecificationTemplate.SubAccountSpec specification) {
        return subAccountRepository.findAll(specification)
                .stream()
                .filter(subAccount -> subAccount.getAccount().getNumber().equalsIgnoreCase("32"))
                .collect(Collectors.toList());
    }

    @Override
    public List<NatureDescriptionResponse> findAllNaturesByProviders(UUID providerId) {
        List<NatureDescriptionResponse> descriptionResponses = new ArrayList<>();
         var returnList = providerNatureRepository.findAllNatureForProvider(providerId).stream()
                 .map(nature -> nature.getNature())
                 .collect(Collectors.toList());

        returnList.forEach(natureDescription -> {
            NatureDescriptionResponse nature = new NatureDescriptionResponse();
            nature.setNatureId(natureDescription.getNatureId());
            nature.setDescription(natureDescription.getDescription());
            descriptionResponses.add(nature);
        });

        return descriptionResponses;
    }

    @Override
    public void associateNatureToProvider(UUID providerId, List<UUID> natureIds) {
        var provider = fetchOrFail(providerId);
        natureIds.forEach( natureId -> {
            ProviderNature providerNature = new ProviderNature();
            if(!providerNatureRepository.getIfExist(providerId,natureId).isPresent()) {
                providerNature.setNature( natureService.fetchOrFail(natureId).get());
                providerNature.setSubAccount(provider.get());
                providerNatureRepository.save(providerNature);
            }

        });
    }

    @Override
    public void validateSubAccount(SubAccount subAccount, UUID accountId) {

        String  accountNumber = "";
        var account = accountService.fetchOrFail(accountId);
        subAccount.setAccount(account.get());

        if (account.get().getDescription().equals("FORNECEDORES")) {
             accountNumber = getNewPrefixeNumber(constants.SUPPLIER);
        } else if (account.get().getDescription().equals("CAIXA")){
            accountNumber = getNewPrefixeNumber(constants.CASH);
        } else if (account.get().getDescription().equals("CLIENTES")) {
            accountNumber = getNewPrefixeNumber(constants.COSTUMER);
        }

        subAccount.setNumber(accountNumber);

        var accountDescriptionOptional = subAccountRepository.findByDescription(subAccount.getDescription());
        if (accountDescriptionOptional.isPresent())
            throw new ExistingDataException("Já existe uma subconta registada com esta descrição");

        var accountNumberOptional = subAccountRepository.findByNumber(subAccount.getNumber());
        if (accountNumberOptional.isPresent())
            throw new ExistingDataException("Já existe uma subconta registada com este número");
    }


    public String getNewPrefixeNumber(String prefixo) {

        String number = "",newPrefixe ="", last;
        int pointPosition = 0, newNumber;
        int maxNumber = 1;

        List<SubAccount> subAccounts = subAccountRepository.findSubAccountsByPrefix(prefixo);
        Optional<SubAccount> subAccount = subAccounts.stream().findFirst();

        number = subAccount.get().getNumber();
        pointPosition = number.lastIndexOf(".");
        last = number.substring(pointPosition + 1);
        newNumber = Integer.parseInt(last);

        if (newNumber > maxNumber) {
            maxNumber = newNumber;
        }

        newPrefixe = prefixo + "" +(maxNumber+1);

        return newPrefixe;
    }
}
