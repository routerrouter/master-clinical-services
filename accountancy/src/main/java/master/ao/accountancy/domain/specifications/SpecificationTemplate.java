package master.ao.accountancy.domain.specifications;

import master.ao.accountancy.domain.models.Account;
import master.ao.accountancy.domain.models.AccountClass;
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



}
