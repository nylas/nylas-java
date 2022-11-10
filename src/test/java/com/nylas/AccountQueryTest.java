package com.nylas;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountQueryTest {

    private AccountQuery accountQuery;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        accountQuery = new AccountQuery();
        MetadataQuery metadataQuery = new MetadataQuery();
        metadataQuery.metadataKey("key1", "key2");
        metadataQuery.metadataValue("value1", "value2");

        FieldSetter.setField("metadataQuery", metadataQuery, accountQuery);
    }

    @Test
    public void testAddParameters() {
        HttpUrl.Builder url = new HttpUrl.Builder();

        url.scheme("https")
                .host("api.nylas.com");

        accountQuery.addParameters(url);

        HttpUrl built = url.build();

        assertNotNull(built);
        assertEquals(built.toString(), "https://api.nylas.com/?metadata_key=key1&metadata_key=key2&metadata_value=value1&metadata_value=value2");
    }

    @Test
    public void testMetadataQuery() {
        accountQuery.metadataQuery(null);

        assertNotNull(accountQuery);
    }
}
