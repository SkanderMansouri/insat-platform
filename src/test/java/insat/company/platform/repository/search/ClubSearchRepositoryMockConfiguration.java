package insat.company.platform.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ClubSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClubSearchRepositoryMockConfiguration {

    @MockBean
    private ClubSearchRepository mockClubSearchRepository;

}
