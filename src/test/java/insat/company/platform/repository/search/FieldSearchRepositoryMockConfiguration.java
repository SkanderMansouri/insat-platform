package insat.company.platform.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FieldSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FieldSearchRepositoryMockConfiguration {

    @MockBean
    private FieldSearchRepository mockFieldSearchRepository;

}
