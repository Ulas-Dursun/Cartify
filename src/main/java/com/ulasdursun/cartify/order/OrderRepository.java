package com.ulasdursun.cartify.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        LEFT JOIN FETCH o.user
        WHERE o.id = :orderId
    """)
    Optional<Order> findDetailedById(@Param("orderId") UUID orderId);

    @Query("""
        SELECT DISTINCT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        WHERE o.user.id = :userId
    """)
    List<Order> findAllDetailedByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT DISTINCT o FROM Order o
    LEFT JOIN FETCH o.items i
    LEFT JOIN FETCH i.product
    LEFT JOIN FETCH o.user
""")
    List<Order> findAllDetailed();
}
