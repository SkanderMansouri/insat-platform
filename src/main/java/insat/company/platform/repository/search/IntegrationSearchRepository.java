package insat.company.platform.repository.search;

import insat.company.platform.domain.Integration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Integration entity.
 */
public interface IntegrationSearchRepository extends ElasticsearchRepository<Integration, Long> {
}
