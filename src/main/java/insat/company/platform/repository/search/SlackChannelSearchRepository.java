package insat.company.platform.repository.search;

import insat.company.platform.domain.SlackChannel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SlackChannel entity.
 */
public interface SlackChannelSearchRepository extends ElasticsearchRepository<SlackChannel, Long> {
}
