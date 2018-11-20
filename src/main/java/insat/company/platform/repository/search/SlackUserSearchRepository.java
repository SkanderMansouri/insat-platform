package insat.company.platform.repository.search;

import insat.company.platform.domain.SlackUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SlackUser entity.
 */
public interface SlackUserSearchRepository extends ElasticsearchRepository<SlackUser, Long> {
}
