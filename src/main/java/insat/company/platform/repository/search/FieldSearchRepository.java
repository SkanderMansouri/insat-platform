package insat.company.platform.repository.search;

import insat.company.platform.domain.Field;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Field entity.
 */
public interface FieldSearchRepository extends ElasticsearchRepository<Field, Long> {
}
