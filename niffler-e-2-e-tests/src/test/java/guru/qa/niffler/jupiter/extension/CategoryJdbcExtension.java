package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        return CategoryJson.fromEntity(spendRepository.createCategory(CategoryEntity.fromJson(category)));
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        spendRepository.removeCategory(CategoryEntity.fromJson(category));
    }
}
