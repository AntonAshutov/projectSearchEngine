package searchengine.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Site;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SiteRepository extends JpaRepository<Site, Integer> {
    Optional<Site> findByUrl(String url);
    void deleteByUrlIn(List<String> urls);


    @Modifying
    @Query("UPDATE Site s SET s.statusTime = :statusTime WHERE s.id = :id")
    void updateStatusTimeById(@Param("id") Integer id, @Param("statusTime") Date statusTime);

    @Modifying
    @Query("UPDATE Site s SET s.status = :status WHERE s.id = :id")
    void updateStatusById(@Param("id") Integer id, @Param("status") Site.SiteStatus status);

    @Modifying
    @Query("UPDATE Site s SET s.lastError = :error WHERE s.id = :id")
    void updateErrorById(@Param("id") Integer id, @Param("error") String error);
}
