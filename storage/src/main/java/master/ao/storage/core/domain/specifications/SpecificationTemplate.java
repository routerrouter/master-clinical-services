package master.ao.storage.core.domain.specifications;

import master.ao.storage.core.domain.models.*;
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
            @Spec(path = "userGroup", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface GroupSpec extends Specification<Group> {
    }

    @And({
            @Spec(path = "userGroup", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CategorySpec extends Specification<Category> {
    }

    @And({
            @Spec(path = "name", spec = Like.class)
    })
    public interface NatureSpec extends Specification<Nature> {
    }

    @And({
            @Spec(path = "userGroup", spec = Equal.class),
            @Spec(path = "description", spec = Like.class)
    })
    public interface StorageSpec extends Specification<Storage> {
    }

    @And({
            @Spec(path = "description", spec = Like.class),
            @Spec(path = "shelf", spec = Like.class),
            @Spec(path = "partition", spec = Like.class)
    })
    public interface LocationSpec extends Specification<Location> {
    }

    public static Specification<Location> locationStorageId(final UUID storageId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Location> location = root;
            Root<Storage> storage = query.from(Storage.class);
            Expression<Collection<Location>> locationsStorageList = storage.get("locations");
            return cb.and(cb.equal(storage.get("storageId"), storageId), cb.isMember(location, locationsStorageList));
        };
    }

    @And({
            @Spec(path = "name", spec = Like.class),
            @Spec(path = "phoneNumber", spec = Like.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "nif", spec = Like.class),
            @Spec(path = "entityType", spec = Equal.class),
            @Spec(path = "enabled", spec = Equal.class)
    })
    public interface EntitySpec extends Specification<Entities> {
    }

    @And({
            @Spec(path = "movementDate", spec = Equal.class),
            @Spec(path = "movementType", spec = Equal.class),
            @Spec(path = "documentNumber", spec = Like.class),
            @Spec(path = "movementStatus", spec = Equal.class),
            @Spec(path = "devolutionType", spec = Equal.class),
            @Spec(path = "userGroup", spec = Equal.class)
    })
    public interface MovementSpec extends Specification<Movement> {
    }

    public static Specification<Movement> movementEntityId(final UUID entityId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Movement> movement = root;
            Root<Entities> entity = query.from(Entities.class);
            Expression<Collection<Movement>> movementsEntityList = entity.get("movements");
            return cb.and(cb.equal(entity.get("entityId"), entityId), cb.isMember(movement, movementsEntityList));
        };
    }

    @And({
            @Spec(path = "natureId", spec = Equal.class),
            @Spec(path = "userGroup", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface ProductSpec extends Specification<Product> {
    }

    public static Specification<Product> productGroupId(final UUID groupId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Product> product = root;
            Root<Group> group = query.from(Group.class);
            Expression<Collection<Product>> productsGroupList = group.get("products");
            return cb.and(cb.equal(group.get("groupId"), groupId), cb.isMember(product, productsGroupList));
        };
    }

    public static Specification<Product> productCategoryId(final UUID categoryId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Product> product = root;
            Root<Category> category = query.from(Category.class);
            Expression<Collection<Product>> productsCategoryList = category.get("products");
            return cb.and(cb.equal(category.get("categoryId"), categoryId), cb.isMember(product, productsCategoryList));
        };
    }





}
