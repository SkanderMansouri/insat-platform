package insat.company.platform.repository.search;

import insat.company.platform.domain.Club;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Club entity.
 */
public interface ClubSearchRepository extends ElasticsearchRepository<Club, Long> {
}
