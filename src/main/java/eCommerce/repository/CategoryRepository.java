package eCommerce.repository;

import eCommerce.model.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths = {"subCategories"})
    List<Category> findByNameContainingIgnoreCase(String keyword);

    @EntityGraph(attributePaths = {"subCategories",
            "subCategories.subCategories"})
    Optional<Category> findWithSubCategoriesById(Long id);
}
