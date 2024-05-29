package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity categoryEntity;

    @Override
    public String toString() {
        return "SpendEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", currency=" + currency +
                ", spendDate=" + spendDate +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", categoryEntity=" + categoryEntity +
                '}';
    }
}
