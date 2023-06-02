package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.CompanyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyImageRepository extends JpaRepository<CompanyImage, UUID> {

}
