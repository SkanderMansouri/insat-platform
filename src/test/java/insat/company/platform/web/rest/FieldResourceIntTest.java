package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.Field;
import insat.company.platform.repository.FieldRepository;
import insat.company.platform.repository.search.FieldSearchRepository;
import insat.company.platform.service.FieldService;
import insat.company.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FieldResource REST controller.
 *
 * @see FieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class FieldResourceIntTest {

    private static final Long DEFAULT_YEAR = 1L;
    private static final Long UPDATED_YEAR = 2L;

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldService fieldService;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.FieldSearchRepositoryMockConfiguration
     */
    @Autowired
    private FieldSearchRepository mockFieldSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFieldMockMvc;

    private Field field;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FieldResource fieldResource = new FieldResource(fieldService);
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createEntity(EntityManager em) {
        Field field = new Field()
            .year(DEFAULT_YEAR)
            .section(DEFAULT_SECTION);
        return field;
    }

    @Before
    public void initTest() {
        field = createEntity(em);
    }

    @Test
    @Transactional
    public void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field
        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testField.getSection()).isEqualTo(DEFAULT_SECTION);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).save(testField);
    }

    @Test
    @Transactional
    public void createFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field with an existing ID
        field.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    public void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList
        restFieldMockMvc.perform(get("/api/fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())));
    }
    
    @Test
    @Transactional
    public void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateField() throws Exception {
        // Initialize the database
        fieldService.save(field);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFieldSearchRepository);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findById(field.getId()).get();
        // Disconnect from session so that the updates on updatedField are not directly saved in db
        em.detach(updatedField);
        updatedField
            .year(UPDATED_YEAR)
            .section(UPDATED_SECTION);

        restFieldMockMvc.perform(put("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedField)))
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testField.getSection()).isEqualTo(UPDATED_SECTION);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).save(testField);
    }

    @Test
    @Transactional
    public void updateNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Create the Field

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc.perform(put("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    public void deleteField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Get the field
        restFieldMockMvc.perform(delete("/api/fields/{id}", field.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).deleteById(field.getId());
    }

    @Test
    @Transactional
    public void searchField() throws Exception {
        // Initialize the database
        fieldService.save(field);
        when(mockFieldSearchRepository.search(queryStringQuery("id:" + field.getId())))
            .thenReturn(Collections.singletonList(field));
        // Search the field
        restFieldMockMvc.perform(get("/api/_search/fields?query=id:" + field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Field.class);
        Field field1 = new Field();
        field1.setId(1L);
        Field field2 = new Field();
        field2.setId(field1.getId());
        assertThat(field1).isEqualTo(field2);
        field2.setId(2L);
        assertThat(field1).isNotEqualTo(field2);
        field1.setId(null);
        assertThat(field1).isNotEqualTo(field2);
    }
}
