package insat.company.platform.repository.search;

import insat.company.platform.domain.InsatEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsatEvent entity.
 */
public interface InsatEventSearchRepository extends ElasticsearchRepository<InsatEvent, Long> {
}
