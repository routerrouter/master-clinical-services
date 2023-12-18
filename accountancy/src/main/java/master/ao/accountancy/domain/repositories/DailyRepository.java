package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.Account;
import master.ao.accountancy.domain.models.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyRepository extends JpaRepository<Daily, UUID> {

    @Query("select d From Daily  d where d.subAccount.subAccountId=?1")
    List<Daily> findDailyByAccount(UUID accountId);
}
