package com.gkrs.fintrek.fintrek_app.repositories;

import com.gkrs.fintrek.fintrek_app.entity.Category;
import com.gkrs.fintrek.fintrek_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c JOIN FETCH c.user WHERE c.categoryName = :categoryName AND c.user = :user")
    Optional<Category> findByCategoryNameAndUser(String categoryName, User user);
}
