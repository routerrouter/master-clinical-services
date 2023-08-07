package master.ao.accountancy.domain.specifications;

import master.ao.accountancy.domain.models.AccountClass;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

   @And({
            @Spec(path = "number", spec = Like.class),
            @Spec(path = "description", spec = Like.class)
    })
    public interface AccountClassSpec extends Specification<AccountClass> {
    }



    /*public static Specification<Location> locationStorageId(final UUID storageId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Location> location = root;
            Root<Storage> storage = query.from(Storage.class);
            Expression<Collection<Location>> locationsStorageList = storage.get("locations");
            return cb.and(cb.equal(storage.get("storageId"), storageId), cb.isMember(location, locationsStorageList));
        };
    }*/



}
