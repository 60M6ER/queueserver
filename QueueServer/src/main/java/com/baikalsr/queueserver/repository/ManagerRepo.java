package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {
    Manager findByName(String name);
    Manager findByLoginAD(String name);



    @Query(value = "SELECT *\n" +
            "    FROM manager\n" +
            "    where loginad = :login_ad limit 1",
            nativeQuery = true)
    Manager findFirstByLoginAD(@Param("login_ad") String name);

    @Query(value = "WITH \n" +
            " manager_status_date AS (\n" +
            "\tselect ms2.manager_id, MAX(ms2.date) as date\n" +
            "\tfrom managers_status ms2 \n" +
            "\twhere ms2.manager_id in (select tsm.managers_id \n" +
            " from ticket_service_managers tsm left outer join manager m2 \n" +
            " on  tsm.managers_id = m2.id \n" +
            " where tsm.ticket_services_id = :ticketServiceId \n" +
            " and m2.queue_id = :queueId) \n" +
            "\tgroup by ms2.manager_id\n" +
            " )\n" +
            " \n" +
            " \n" +
            " select * \n" +
            " from manager m join managers_status ms\n" +
            " on (m.id = ms.manager_id)\n" +
            " join manager_status_date msd\n" +
            " on (ms.manager_id = msd.manager_id\n" +
            "\tand ms.date = msd.date\n" +
            "\tand ms.status = 'WORKING_TIME')\n" +
            "  order by ms.date limit 1",
            nativeQuery = true)
    Manager getManagerByTicketServiceToDistrib(@Param("ticketServiceId") Long ticketServiceId, @Param("queueId") Long queueId);

    List<Manager> findAllByQueueAndActiveOrderByName(Queue queue, Boolean active);

    @Query(value = "WITH managers AS (SELECT *\n" +
            "\tFROM manager m\n" +
            "\tWHERE m.queue_id isNull),\n" +
            "\tmanager_service AS(SELECT m.id,\n" +
            "\t\t\t\t\t   m.active,\n" +
            "\t\t\t\t\t   m.loginad,\n" +
            "\t\t\t\t\t   m.name,\n" +
            "\t\t\t\t\t   m.password,\n" +
            "\t\t\t\t\t   m.queue_id,\n" +
            "\t\t\t\t\t   m.rest_token,\n" +
            "\t\t\t\t\t   CASE \n" +
            "\t\t\t\t\t   \tWHEN ur.roles = 'SERVICE' THEN 1\n" +
            "\t\t\t\t\t   \tELSE 0\n" +
            "\t\t\t\t\t   END AS service\n" +
            "\t\t\t\t\t  FROM managers m JOIN user_role ur\n" +
            "\t\t\t\t\t  ON(m.id = ur.user_id)),\n" +
            "\tmanagers_final AS (SELECT ms.id,\n" +
            "\t\t\t\t\t   ms.active,\n" +
            "\t\t\t\t\t   ms.loginad,\n" +
            "\t\t\t\t\t   ms.name,\n" +
            "\t\t\t\t\t   ms.password,\n" +
            "\t\t\t\t\t   ms.queue_id,\n" +
            "\t\t\t\t\t   ms.rest_token,\n" +
            "\t\t\t\t\t   MAX(ms.service) AS service\n" +
            "\t\t\t\t\t  FROM manager_service ms \n" +
            "\t\t\t\t\t  GROUP BY ms.id,\n" +
            "\t\t\t\t\t   ms.active,\n" +
            "\t\t\t\t\t   ms.loginad,\n" +
            "\t\t\t\t\t   ms.name,\n" +
            "\t\t\t\t\t   ms.password,\n" +
            "\t\t\t\t\t   ms.queue_id,\n" +
            "\t\t\t\t\t   ms.rest_token)\t\t\t\t\t  \n" +
            "SELECT *\n" +
            "FROM managers_final mf\n" +
            "WHERE mf.service = 0\n" +
            "ORDER BY mf.name\n" +
            ";",
            nativeQuery = true)
    List<Manager> findAllWithoutQueueOrderByName();
}
