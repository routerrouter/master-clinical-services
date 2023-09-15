package master.ao.accountancy.domain.specifications;

import master.ao.accountancy.domain.models.*;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "number", spec = Like.class),
            @Spec(path = "description", spec = Like.class)
    })
    public interface AccountClassSpec extends Specification<AccountClass> {
    }

    @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface CategorySpec extends Specification<Category> {
    }

    @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface NatureSpec extends Specification<AccountNature> {
    }

    @And({
            @Spec(path = "number", spec = Like.class),
            @Spec(path = "description", spec = Like.class),
            @Spec(path = "accountType", spec = Equal.class)
    })
    public interface AccountSpec extends Specification<Account> {
    }

    public static Specification<Account> accountByClassId(final UUID classId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Account> account = root;
            Root<AccountClass> accountClassRoot = query.from(AccountClass.class);
            Expression<Collection<Account>>  accountsCollection = accountClassRoot.get("accounts");
            return cb.and(cb.equal(accountClassRoot.get("classId"), classId), cb.isMember(account, accountsCollection));
        };
    }

    @And({
            @Spec(path = "number", spec = Like.class),
            @Spec(path = "description", spec = Like.class),
    })
    public interface SubAccountSpec extends Specification<SubAccount> {
    }

    public static Specification<SubAccount> subAccountByAccountId(final UUID accountId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<SubAccount> subAccount = root;
            Root<Account> account = query.from(Account.class);
            Expression<Collection<SubAccount>>  subAccountsCollection = account.get("subAccounts");
            return cb.and(cb.equal(account.get("accountId"), accountId), cb.isMember(subAccount, subAccountsCollection));
        };
    }



}
