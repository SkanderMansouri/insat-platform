package insat.company.platform.repository.search;

import insat.company.platform.domain.JoinClubRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JoinClubRequest entity.
 */
public interface JoinClubRequestSearchRepository extends ElasticsearchRepository<JoinClubRequest, Long> {
}
