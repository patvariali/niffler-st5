package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpendJdbcExtension extends AbstractSpendExtension{

    private final SpendRepository spendRepository = SpendRepository.getInstance();


    @Override
    protected SpendJson createSpend(SpendJson spend) {
        CategoryEntity categoryEntity = spendRepository.findByUsernameAndCategory(spend.username(), spend.category());

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(spend.username());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        spendEntity.setCategoryEntity(categoryEntity);

        return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(spend.id());
        spendRepository.removeSpend(spendEntity);
    }


}
