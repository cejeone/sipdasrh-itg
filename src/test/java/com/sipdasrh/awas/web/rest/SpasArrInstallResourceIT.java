package com.sipdasrh.awas.web.rest;

import static com.sipdasrh.awas.domain.SpasArrInstallAsserts.*;
import static com.sipdasrh.awas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipdasrh.awas.IntegrationTest;
import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.repository.SpasArrInstallRepository;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import com.sipdasrh.awas.service.mapper.SpasArrInstallMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpasArrInstallResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpasArrInstallResourceIT {

    private static final String DEFAULT_NAMA_INSTALASI = "AAAAAAAAAA";
    private static final String UPDATED_NAMA_INSTALASI = "BBBBBBBBBB";

    private static final String DEFAULT_URL_INSTALASI = "AAAAAAAAAA";
    private static final String UPDATED_URL_INSTALASI = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT_INSTALASI = 1D;
    private static final Double UPDATED_LAT_INSTALASI = 2D;
    private static final Double SMALLER_LAT_INSTALASI = 1D - 1D;

    private static final Double DEFAULT_LONG_INSTALASI = 1D;
    private static final Double UPDATED_LONG_INSTALASI = 2D;
    private static final Double SMALLER_LONG_INSTALASI = 1D - 1D;

    private static final Double DEFAULT_THRESHOLD_INSTALASI = 1D;
    private static final Double UPDATED_THRESHOLD_INSTALASI = 2D;
    private static final Double SMALLER_THRESHOLD_INSTALASI = 1D - 1D;

    private static final Integer DEFAULT_URL_EWS_GIS = 1;
    private static final Integer UPDATED_URL_EWS_GIS = 2;
    private static final Integer SMALLER_URL_EWS_GIS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/spas-arr-installs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpasArrInstallRepository spasArrInstallRepository;

    @Autowired
    private SpasArrInstallMapper spasArrInstallMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpasArrInstallMockMvc;

    private SpasArrInstall spasArrInstall;

    private SpasArrInstall insertedSpasArrInstall;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpasArrInstall createEntity() {
        return new SpasArrInstall()
            .namaInstalasi(DEFAULT_NAMA_INSTALASI)
            .urlInstalasi(DEFAULT_URL_INSTALASI)
            .latInstalasi(DEFAULT_LAT_INSTALASI)
            .longInstalasi(DEFAULT_LONG_INSTALASI)
            .thresholdInstalasi(DEFAULT_THRESHOLD_INSTALASI)
            .urlEwsGis(DEFAULT_URL_EWS_GIS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpasArrInstall createUpdatedEntity() {
        return new SpasArrInstall()
            .namaInstalasi(UPDATED_NAMA_INSTALASI)
            .urlInstalasi(UPDATED_URL_INSTALASI)
            .latInstalasi(UPDATED_LAT_INSTALASI)
            .longInstalasi(UPDATED_LONG_INSTALASI)
            .thresholdInstalasi(UPDATED_THRESHOLD_INSTALASI)
            .urlEwsGis(UPDATED_URL_EWS_GIS);
    }

    @BeforeEach
    void initTest() {
        spasArrInstall = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpasArrInstall != null) {
            spasArrInstallRepository.delete(insertedSpasArrInstall);
            insertedSpasArrInstall = null;
        }
    }

    @Test
    @Transactional
    void createSpasArrInstall() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);
        var returnedSpasArrInstallDTO = om.readValue(
            restSpasArrInstallMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrInstallDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpasArrInstallDTO.class
        );

        // Validate the SpasArrInstall in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpasArrInstall = spasArrInstallMapper.toEntity(returnedSpasArrInstallDTO);
        assertSpasArrInstallUpdatableFieldsEquals(returnedSpasArrInstall, getPersistedSpasArrInstall(returnedSpasArrInstall));

        insertedSpasArrInstall = returnedSpasArrInstall;
    }

    @Test
    @Transactional
    void createSpasArrInstallWithExistingId() throws Exception {
        // Create the SpasArrInstall with an existing ID
        spasArrInstall.setId(1L);
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpasArrInstallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrInstallDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpasArrInstalls() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spasArrInstall.getId().intValue())))
            .andExpect(jsonPath("$.[*].namaInstalasi").value(hasItem(DEFAULT_NAMA_INSTALASI)))
            .andExpect(jsonPath("$.[*].urlInstalasi").value(hasItem(DEFAULT_URL_INSTALASI)))
            .andExpect(jsonPath("$.[*].latInstalasi").value(hasItem(DEFAULT_LAT_INSTALASI)))
            .andExpect(jsonPath("$.[*].longInstalasi").value(hasItem(DEFAULT_LONG_INSTALASI)))
            .andExpect(jsonPath("$.[*].thresholdInstalasi").value(hasItem(DEFAULT_THRESHOLD_INSTALASI)))
            .andExpect(jsonPath("$.[*].urlEwsGis").value(hasItem(DEFAULT_URL_EWS_GIS)));
    }

    @Test
    @Transactional
    void getSpasArrInstall() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get the spasArrInstall
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL_ID, spasArrInstall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spasArrInstall.getId().intValue()))
            .andExpect(jsonPath("$.namaInstalasi").value(DEFAULT_NAMA_INSTALASI))
            .andExpect(jsonPath("$.urlInstalasi").value(DEFAULT_URL_INSTALASI))
            .andExpect(jsonPath("$.latInstalasi").value(DEFAULT_LAT_INSTALASI))
            .andExpect(jsonPath("$.longInstalasi").value(DEFAULT_LONG_INSTALASI))
            .andExpect(jsonPath("$.thresholdInstalasi").value(DEFAULT_THRESHOLD_INSTALASI))
            .andExpect(jsonPath("$.urlEwsGis").value(DEFAULT_URL_EWS_GIS));
    }

    @Test
    @Transactional
    void getSpasArrInstallsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        Long id = spasArrInstall.getId();

        defaultSpasArrInstallFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSpasArrInstallFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSpasArrInstallFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByNamaInstalasiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where namaInstalasi equals to
        defaultSpasArrInstallFiltering("namaInstalasi.equals=" + DEFAULT_NAMA_INSTALASI, "namaInstalasi.equals=" + UPDATED_NAMA_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByNamaInstalasiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where namaInstalasi in
        defaultSpasArrInstallFiltering(
            "namaInstalasi.in=" + DEFAULT_NAMA_INSTALASI + "," + UPDATED_NAMA_INSTALASI,
            "namaInstalasi.in=" + UPDATED_NAMA_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByNamaInstalasiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where namaInstalasi is not null
        defaultSpasArrInstallFiltering("namaInstalasi.specified=true", "namaInstalasi.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByNamaInstalasiContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where namaInstalasi contains
        defaultSpasArrInstallFiltering(
            "namaInstalasi.contains=" + DEFAULT_NAMA_INSTALASI,
            "namaInstalasi.contains=" + UPDATED_NAMA_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByNamaInstalasiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where namaInstalasi does not contain
        defaultSpasArrInstallFiltering(
            "namaInstalasi.doesNotContain=" + UPDATED_NAMA_INSTALASI,
            "namaInstalasi.doesNotContain=" + DEFAULT_NAMA_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlInstalasiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlInstalasi equals to
        defaultSpasArrInstallFiltering("urlInstalasi.equals=" + DEFAULT_URL_INSTALASI, "urlInstalasi.equals=" + UPDATED_URL_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlInstalasiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlInstalasi in
        defaultSpasArrInstallFiltering(
            "urlInstalasi.in=" + DEFAULT_URL_INSTALASI + "," + UPDATED_URL_INSTALASI,
            "urlInstalasi.in=" + UPDATED_URL_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlInstalasiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlInstalasi is not null
        defaultSpasArrInstallFiltering("urlInstalasi.specified=true", "urlInstalasi.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlInstalasiContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlInstalasi contains
        defaultSpasArrInstallFiltering("urlInstalasi.contains=" + DEFAULT_URL_INSTALASI, "urlInstalasi.contains=" + UPDATED_URL_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlInstalasiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlInstalasi does not contain
        defaultSpasArrInstallFiltering(
            "urlInstalasi.doesNotContain=" + UPDATED_URL_INSTALASI,
            "urlInstalasi.doesNotContain=" + DEFAULT_URL_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi equals to
        defaultSpasArrInstallFiltering("latInstalasi.equals=" + DEFAULT_LAT_INSTALASI, "latInstalasi.equals=" + UPDATED_LAT_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi in
        defaultSpasArrInstallFiltering(
            "latInstalasi.in=" + DEFAULT_LAT_INSTALASI + "," + UPDATED_LAT_INSTALASI,
            "latInstalasi.in=" + UPDATED_LAT_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi is not null
        defaultSpasArrInstallFiltering("latInstalasi.specified=true", "latInstalasi.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi is greater than or equal to
        defaultSpasArrInstallFiltering(
            "latInstalasi.greaterThanOrEqual=" + DEFAULT_LAT_INSTALASI,
            "latInstalasi.greaterThanOrEqual=" + UPDATED_LAT_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi is less than or equal to
        defaultSpasArrInstallFiltering(
            "latInstalasi.lessThanOrEqual=" + DEFAULT_LAT_INSTALASI,
            "latInstalasi.lessThanOrEqual=" + SMALLER_LAT_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi is less than
        defaultSpasArrInstallFiltering("latInstalasi.lessThan=" + UPDATED_LAT_INSTALASI, "latInstalasi.lessThan=" + DEFAULT_LAT_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLatInstalasiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where latInstalasi is greater than
        defaultSpasArrInstallFiltering(
            "latInstalasi.greaterThan=" + SMALLER_LAT_INSTALASI,
            "latInstalasi.greaterThan=" + DEFAULT_LAT_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi equals to
        defaultSpasArrInstallFiltering("longInstalasi.equals=" + DEFAULT_LONG_INSTALASI, "longInstalasi.equals=" + UPDATED_LONG_INSTALASI);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi in
        defaultSpasArrInstallFiltering(
            "longInstalasi.in=" + DEFAULT_LONG_INSTALASI + "," + UPDATED_LONG_INSTALASI,
            "longInstalasi.in=" + UPDATED_LONG_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi is not null
        defaultSpasArrInstallFiltering("longInstalasi.specified=true", "longInstalasi.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi is greater than or equal to
        defaultSpasArrInstallFiltering(
            "longInstalasi.greaterThanOrEqual=" + DEFAULT_LONG_INSTALASI,
            "longInstalasi.greaterThanOrEqual=" + UPDATED_LONG_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi is less than or equal to
        defaultSpasArrInstallFiltering(
            "longInstalasi.lessThanOrEqual=" + DEFAULT_LONG_INSTALASI,
            "longInstalasi.lessThanOrEqual=" + SMALLER_LONG_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi is less than
        defaultSpasArrInstallFiltering(
            "longInstalasi.lessThan=" + UPDATED_LONG_INSTALASI,
            "longInstalasi.lessThan=" + DEFAULT_LONG_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByLongInstalasiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where longInstalasi is greater than
        defaultSpasArrInstallFiltering(
            "longInstalasi.greaterThan=" + SMALLER_LONG_INSTALASI,
            "longInstalasi.greaterThan=" + DEFAULT_LONG_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi equals to
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.equals=" + DEFAULT_THRESHOLD_INSTALASI,
            "thresholdInstalasi.equals=" + UPDATED_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi in
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.in=" + DEFAULT_THRESHOLD_INSTALASI + "," + UPDATED_THRESHOLD_INSTALASI,
            "thresholdInstalasi.in=" + UPDATED_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi is not null
        defaultSpasArrInstallFiltering("thresholdInstalasi.specified=true", "thresholdInstalasi.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi is greater than or equal to
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.greaterThanOrEqual=" + DEFAULT_THRESHOLD_INSTALASI,
            "thresholdInstalasi.greaterThanOrEqual=" + UPDATED_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi is less than or equal to
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.lessThanOrEqual=" + DEFAULT_THRESHOLD_INSTALASI,
            "thresholdInstalasi.lessThanOrEqual=" + SMALLER_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi is less than
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.lessThan=" + UPDATED_THRESHOLD_INSTALASI,
            "thresholdInstalasi.lessThan=" + DEFAULT_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByThresholdInstalasiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where thresholdInstalasi is greater than
        defaultSpasArrInstallFiltering(
            "thresholdInstalasi.greaterThan=" + SMALLER_THRESHOLD_INSTALASI,
            "thresholdInstalasi.greaterThan=" + DEFAULT_THRESHOLD_INSTALASI
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis equals to
        defaultSpasArrInstallFiltering("urlEwsGis.equals=" + DEFAULT_URL_EWS_GIS, "urlEwsGis.equals=" + UPDATED_URL_EWS_GIS);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis in
        defaultSpasArrInstallFiltering(
            "urlEwsGis.in=" + DEFAULT_URL_EWS_GIS + "," + UPDATED_URL_EWS_GIS,
            "urlEwsGis.in=" + UPDATED_URL_EWS_GIS
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis is not null
        defaultSpasArrInstallFiltering("urlEwsGis.specified=true", "urlEwsGis.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis is greater than or equal to
        defaultSpasArrInstallFiltering(
            "urlEwsGis.greaterThanOrEqual=" + DEFAULT_URL_EWS_GIS,
            "urlEwsGis.greaterThanOrEqual=" + UPDATED_URL_EWS_GIS
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis is less than or equal to
        defaultSpasArrInstallFiltering(
            "urlEwsGis.lessThanOrEqual=" + DEFAULT_URL_EWS_GIS,
            "urlEwsGis.lessThanOrEqual=" + SMALLER_URL_EWS_GIS
        );
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis is less than
        defaultSpasArrInstallFiltering("urlEwsGis.lessThan=" + UPDATED_URL_EWS_GIS, "urlEwsGis.lessThan=" + DEFAULT_URL_EWS_GIS);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsByUrlEwsGisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        // Get all the spasArrInstallList where urlEwsGis is greater than
        defaultSpasArrInstallFiltering("urlEwsGis.greaterThan=" + SMALLER_URL_EWS_GIS, "urlEwsGis.greaterThan=" + DEFAULT_URL_EWS_GIS);
    }

    @Test
    @Transactional
    void getAllSpasArrInstallsBySpasIsEqualToSomething() throws Exception {
        Spas spas;
        if (TestUtil.findAll(em, Spas.class).isEmpty()) {
            spasArrInstallRepository.saveAndFlush(spasArrInstall);
            spas = SpasResourceIT.createEntity();
        } else {
            spas = TestUtil.findAll(em, Spas.class).get(0);
        }
        em.persist(spas);
        em.flush();
        spasArrInstall.setSpas(spas);
        spasArrInstallRepository.saveAndFlush(spasArrInstall);
        Long spasId = spas.getId();
        // Get all the spasArrInstallList where spas equals to spasId
        defaultSpasArrInstallShouldBeFound("spasId.equals=" + spasId);

        // Get all the spasArrInstallList where spas equals to (spasId + 1)
        defaultSpasArrInstallShouldNotBeFound("spasId.equals=" + (spasId + 1));
    }

    private void defaultSpasArrInstallFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSpasArrInstallShouldBeFound(shouldBeFound);
        defaultSpasArrInstallShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpasArrInstallShouldBeFound(String filter) throws Exception {
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spasArrInstall.getId().intValue())))
            .andExpect(jsonPath("$.[*].namaInstalasi").value(hasItem(DEFAULT_NAMA_INSTALASI)))
            .andExpect(jsonPath("$.[*].urlInstalasi").value(hasItem(DEFAULT_URL_INSTALASI)))
            .andExpect(jsonPath("$.[*].latInstalasi").value(hasItem(DEFAULT_LAT_INSTALASI)))
            .andExpect(jsonPath("$.[*].longInstalasi").value(hasItem(DEFAULT_LONG_INSTALASI)))
            .andExpect(jsonPath("$.[*].thresholdInstalasi").value(hasItem(DEFAULT_THRESHOLD_INSTALASI)))
            .andExpect(jsonPath("$.[*].urlEwsGis").value(hasItem(DEFAULT_URL_EWS_GIS)));

        // Check, that the count call also returns 1
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpasArrInstallShouldNotBeFound(String filter) throws Exception {
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpasArrInstallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpasArrInstall() throws Exception {
        // Get the spasArrInstall
        restSpasArrInstallMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpasArrInstall() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrInstall
        SpasArrInstall updatedSpasArrInstall = spasArrInstallRepository.findById(spasArrInstall.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpasArrInstall are not directly saved in db
        em.detach(updatedSpasArrInstall);
        updatedSpasArrInstall
            .namaInstalasi(UPDATED_NAMA_INSTALASI)
            .urlInstalasi(UPDATED_URL_INSTALASI)
            .latInstalasi(UPDATED_LAT_INSTALASI)
            .longInstalasi(UPDATED_LONG_INSTALASI)
            .thresholdInstalasi(UPDATED_THRESHOLD_INSTALASI)
            .urlEwsGis(UPDATED_URL_EWS_GIS);
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(updatedSpasArrInstall);

        restSpasArrInstallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spasArrInstallDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrInstallDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpasArrInstallToMatchAllProperties(updatedSpasArrInstall);
    }

    @Test
    @Transactional
    void putNonExistingSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spasArrInstallDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrInstallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrInstallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrInstallDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpasArrInstallWithPatch() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrInstall using partial update
        SpasArrInstall partialUpdatedSpasArrInstall = new SpasArrInstall();
        partialUpdatedSpasArrInstall.setId(spasArrInstall.getId());

        partialUpdatedSpasArrInstall
            .latInstalasi(UPDATED_LAT_INSTALASI)
            .longInstalasi(UPDATED_LONG_INSTALASI)
            .thresholdInstalasi(UPDATED_THRESHOLD_INSTALASI);

        restSpasArrInstallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpasArrInstall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpasArrInstall))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrInstall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasArrInstallUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpasArrInstall, spasArrInstall),
            getPersistedSpasArrInstall(spasArrInstall)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpasArrInstallWithPatch() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrInstall using partial update
        SpasArrInstall partialUpdatedSpasArrInstall = new SpasArrInstall();
        partialUpdatedSpasArrInstall.setId(spasArrInstall.getId());

        partialUpdatedSpasArrInstall
            .namaInstalasi(UPDATED_NAMA_INSTALASI)
            .urlInstalasi(UPDATED_URL_INSTALASI)
            .latInstalasi(UPDATED_LAT_INSTALASI)
            .longInstalasi(UPDATED_LONG_INSTALASI)
            .thresholdInstalasi(UPDATED_THRESHOLD_INSTALASI)
            .urlEwsGis(UPDATED_URL_EWS_GIS);

        restSpasArrInstallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpasArrInstall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpasArrInstall))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrInstall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasArrInstallUpdatableFieldsEquals(partialUpdatedSpasArrInstall, getPersistedSpasArrInstall(partialUpdatedSpasArrInstall));
    }

    @Test
    @Transactional
    void patchNonExistingSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spasArrInstallDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spasArrInstallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spasArrInstallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpasArrInstall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrInstall.setId(longCount.incrementAndGet());

        // Create the SpasArrInstall
        SpasArrInstallDTO spasArrInstallDTO = spasArrInstallMapper.toDto(spasArrInstall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrInstallMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(spasArrInstallDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpasArrInstall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpasArrInstall() throws Exception {
        // Initialize the database
        insertedSpasArrInstall = spasArrInstallRepository.saveAndFlush(spasArrInstall);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the spasArrInstall
        restSpasArrInstallMockMvc
            .perform(delete(ENTITY_API_URL_ID, spasArrInstall.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return spasArrInstallRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SpasArrInstall getPersistedSpasArrInstall(SpasArrInstall spasArrInstall) {
        return spasArrInstallRepository.findById(spasArrInstall.getId()).orElseThrow();
    }

    protected void assertPersistedSpasArrInstallToMatchAllProperties(SpasArrInstall expectedSpasArrInstall) {
        assertSpasArrInstallAllPropertiesEquals(expectedSpasArrInstall, getPersistedSpasArrInstall(expectedSpasArrInstall));
    }

    protected void assertPersistedSpasArrInstallToMatchUpdatableProperties(SpasArrInstall expectedSpasArrInstall) {
        assertSpasArrInstallAllUpdatablePropertiesEquals(expectedSpasArrInstall, getPersistedSpasArrInstall(expectedSpasArrInstall));
    }
}
