import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);

        CloseableHttpResponse response = httpClient.execute(request);

        ObjectMapper mapper = new ObjectMapper();
        List<CatFact> facts = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<List<CatFact>>() {}
        );

        List<CatFact> filtered = facts.stream()
                .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 0)
                .collect(Collectors.toList());

        System.out.println("Всего фактов: " + facts.size());
        System.out.println("Отфильтровано (upvotes > 0): " + filtered.size());
        System.out.println("---");
        filtered.forEach(System.out::println);

        response.close();
        httpClient.close();
    }
}
