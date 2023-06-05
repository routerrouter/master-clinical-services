package master.ao.authuser.core.domain.repositories;

import master.ao.authuser.core.domain.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StorageRepository extends JpaRepository<Storage, UUID> {

    List<Storage> findStorageByUserGroup(UUID userGroup);

    @Modifying
    @Query(value = "UPDATE Storage s set s.name=:name, s.userGroup=:userGroup " +
            "where s.storageId=:storageId")
    void updateStorage(UUID storageId , String name, UUID userGroup);

    @Modifying
    @Query(value = "INSERT INTO tb_storages(storage_id,name,user_group)  " +
            "values(:storageId,:name,:userGroup)",nativeQuery = true)
    void saveStorage(UUID storageId , String name, UUID userGroup);

}