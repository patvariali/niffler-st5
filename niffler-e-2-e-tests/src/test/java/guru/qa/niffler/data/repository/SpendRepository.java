package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.List;
import java.util.UUID;

public interface SpendRepository {

    static SpendRepository getInstance() {
        if ("sjdbc".equals(System.getProperty("repo"))){
            return new SpendRepositorySprintJdbc();
        }
        if ("hibernate".equals(System.getProperty("repo"))){
            return new SpendRepositoryHibernate();
        }
        return new SpendRepositoryJdbc();
    }

    CategoryEntity findByUsernameAndCategory(String username, String category);

    CategoryEntity findByUsernameAndCategoryId(String username, UUID categoryId);


    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

    List<SpendEntity> findAllByUsername(String username);

}
