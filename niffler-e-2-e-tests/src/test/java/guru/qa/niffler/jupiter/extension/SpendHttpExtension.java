package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpendHttpExtension extends AbstractSpendExtension {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();


    @Override
    protected SpendJson createSpend(SpendJson spend) {
        SpendApi spendApi = retrofit.create(SpendApi.class);
        try {
            spend = Objects.requireNonNull(spendApi.createSpend(spend).execute().body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return spend;
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        SpendApi spendApi = retrofit.create(SpendApi.class);
        spendApi.removeSpend(
                spend.username(),
                new ArrayList<>(List.of(spend.id()))
        );
    }
}
