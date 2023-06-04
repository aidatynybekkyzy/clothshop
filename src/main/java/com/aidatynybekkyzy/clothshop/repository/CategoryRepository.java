package com.aidatynybekkyzy.clothshop.repository;

import com.aidatynybekkyzy.clothshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    @Query("select (count(c) > 0) from Category c where c.categoryName = ?1")
    boolean existsByCategoryName(String name);
}
