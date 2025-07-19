package com.sipdasrh.awas.web.rest;

import static com.sipdasrh.awas.domain.SpasArrLogAsserts.*;
import static com.sipdasrh.awas.web.rest.TestUtil.createUpdateProxyForBean;
import static com.sipdasrh.awas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipdasrh.awas.IntegrationTest;
import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.repository.SpasArrLogRepository;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import com.sipdasrh.awas.service.mapper.SpasArrLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SpasArrLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpasArrLogResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_LOG = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_LOG = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_LOG = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_TIME_RETRIEVE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_RETRIEVE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_RETRIEVE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_LOG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_LOG_VALUE = "BBBBBBBBBB";

    private static final Double DEFAULT_WATER_LEVEL = 1D;
    private static final Double UPDATED_WATER_LEVEL = 2D;
    private static final Double SMALLER_WATER_LEVEL = 1D - 1D;

    private static final Double DEFAULT_BATTERY_LEVEL = 1D;
    private static final Double UPDATED_BATTERY_LEVEL = 2D;
    private static final Double SMALLER_BATTERY_LEVEL = 1D - 1D;

    private static final Double DEFAULT_RAIN_LEVEL = 1D;
    private static final Double UPDATED_RAIN_LEVEL = 2D;
    private static final Double SMALLER_RAIN_LEVEL = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/spas-arr-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpasArrLogRepository spasArrLogRepository;

    @Autowired
    private SpasArrLogMapper spasArrLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpasArrLogMockMvc;

    private SpasArrLog spasArrLog;

    private SpasArrLog insertedSpasArrLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpasArrLog createEntity() {
        return new SpasArrLog()
            .timeLog(DEFAULT_TIME_LOG)
            .timeRetrieve(DEFAULT_TIME_RETRIEVE)
            .logValue(DEFAULT_LOG_VALUE)
            .waterLevel(DEFAULT_WATER_LEVEL)
            .batteryLevel(DEFAULT_BATTERY_LEVEL)
            .rainLevel(DEFAULT_RAIN_LEVEL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpasArrLog createUpdatedEntity() {
        return new SpasArrLog()
            .timeLog(UPDATED_TIME_LOG)
            .timeRetrieve(UPDATED_TIME_RETRIEVE)
            .logValue(UPDATED_LOG_VALUE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .batteryLevel(UPDATED_BATTERY_LEVEL)
            .rainLevel(UPDATED_RAIN_LEVEL);
    }

    @BeforeEach
    void initTest() {
        spasArrLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpasArrLog != null) {
            spasArrLogRepository.delete(insertedSpasArrLog);
            insertedSpasArrLog = null;
        }
    }

    @Test
    @Transactional
    void createSpasArrLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);
        var returnedSpasArrLogDTO = om.readValue(
            restSpasArrLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpasArrLogDTO.class
        );

        // Validate the SpasArrLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpasArrLog = spasArrLogMapper.toEntity(returnedSpasArrLogDTO);
        assertSpasArrLogUpdatableFieldsEquals(returnedSpasArrLog, getPersistedSpasArrLog(returnedSpasArrLog));

        insertedSpasArrLog = returnedSpasArrLog;
    }

    @Test
    @Transactional
    void createSpasArrLogWithExistingId() throws Exception {
        // Create the SpasArrLog with an existing ID
        spasArrLog.setId(1L);
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpasArrLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpasArrLogs() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spasArrLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeLog").value(hasItem(sameInstant(DEFAULT_TIME_LOG))))
            .andExpect(jsonPath("$.[*].timeRetrieve").value(hasItem(sameInstant(DEFAULT_TIME_RETRIEVE))))
            .andExpect(jsonPath("$.[*].logValue").value(hasItem(DEFAULT_LOG_VALUE)))
            .andExpect(jsonPath("$.[*].waterLevel").value(hasItem(DEFAULT_WATER_LEVEL)))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL)))
            .andExpect(jsonPath("$.[*].rainLevel").value(hasItem(DEFAULT_RAIN_LEVEL)));
    }

    @Test
    @Transactional
    void getSpasArrLog() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get the spasArrLog
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL_ID, spasArrLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spasArrLog.getId().intValue()))
            .andExpect(jsonPath("$.timeLog").value(sameInstant(DEFAULT_TIME_LOG)))
            .andExpect(jsonPath("$.timeRetrieve").value(sameInstant(DEFAULT_TIME_RETRIEVE)))
            .andExpect(jsonPath("$.logValue").value(DEFAULT_LOG_VALUE))
            .andExpect(jsonPath("$.waterLevel").value(DEFAULT_WATER_LEVEL))
            .andExpect(jsonPath("$.batteryLevel").value(DEFAULT_BATTERY_LEVEL))
            .andExpect(jsonPath("$.rainLevel").value(DEFAULT_RAIN_LEVEL));
    }

    @Test
    @Transactional
    void getSpasArrLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        Long id = spasArrLog.getId();

        defaultSpasArrLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSpasArrLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSpasArrLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog equals to
        defaultSpasArrLogFiltering("timeLog.equals=" + DEFAULT_TIME_LOG, "timeLog.equals=" + UPDATED_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog in
        defaultSpasArrLogFiltering("timeLog.in=" + DEFAULT_TIME_LOG + "," + UPDATED_TIME_LOG, "timeLog.in=" + UPDATED_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog is not null
        defaultSpasArrLogFiltering("timeLog.specified=true", "timeLog.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog is greater than or equal to
        defaultSpasArrLogFiltering("timeLog.greaterThanOrEqual=" + DEFAULT_TIME_LOG, "timeLog.greaterThanOrEqual=" + UPDATED_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog is less than or equal to
        defaultSpasArrLogFiltering("timeLog.lessThanOrEqual=" + DEFAULT_TIME_LOG, "timeLog.lessThanOrEqual=" + SMALLER_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog is less than
        defaultSpasArrLogFiltering("timeLog.lessThan=" + UPDATED_TIME_LOG, "timeLog.lessThan=" + DEFAULT_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeLogIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeLog is greater than
        defaultSpasArrLogFiltering("timeLog.greaterThan=" + SMALLER_TIME_LOG, "timeLog.greaterThan=" + DEFAULT_TIME_LOG);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve equals to
        defaultSpasArrLogFiltering("timeRetrieve.equals=" + DEFAULT_TIME_RETRIEVE, "timeRetrieve.equals=" + UPDATED_TIME_RETRIEVE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve in
        defaultSpasArrLogFiltering(
            "timeRetrieve.in=" + DEFAULT_TIME_RETRIEVE + "," + UPDATED_TIME_RETRIEVE,
            "timeRetrieve.in=" + UPDATED_TIME_RETRIEVE
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve is not null
        defaultSpasArrLogFiltering("timeRetrieve.specified=true", "timeRetrieve.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve is greater than or equal to
        defaultSpasArrLogFiltering(
            "timeRetrieve.greaterThanOrEqual=" + DEFAULT_TIME_RETRIEVE,
            "timeRetrieve.greaterThanOrEqual=" + UPDATED_TIME_RETRIEVE
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve is less than or equal to
        defaultSpasArrLogFiltering(
            "timeRetrieve.lessThanOrEqual=" + DEFAULT_TIME_RETRIEVE,
            "timeRetrieve.lessThanOrEqual=" + SMALLER_TIME_RETRIEVE
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve is less than
        defaultSpasArrLogFiltering("timeRetrieve.lessThan=" + UPDATED_TIME_RETRIEVE, "timeRetrieve.lessThan=" + DEFAULT_TIME_RETRIEVE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByTimeRetrieveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where timeRetrieve is greater than
        defaultSpasArrLogFiltering(
            "timeRetrieve.greaterThan=" + SMALLER_TIME_RETRIEVE,
            "timeRetrieve.greaterThan=" + DEFAULT_TIME_RETRIEVE
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByLogValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where logValue equals to
        defaultSpasArrLogFiltering("logValue.equals=" + DEFAULT_LOG_VALUE, "logValue.equals=" + UPDATED_LOG_VALUE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByLogValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where logValue in
        defaultSpasArrLogFiltering("logValue.in=" + DEFAULT_LOG_VALUE + "," + UPDATED_LOG_VALUE, "logValue.in=" + UPDATED_LOG_VALUE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByLogValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where logValue is not null
        defaultSpasArrLogFiltering("logValue.specified=true", "logValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByLogValueContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where logValue contains
        defaultSpasArrLogFiltering("logValue.contains=" + DEFAULT_LOG_VALUE, "logValue.contains=" + UPDATED_LOG_VALUE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByLogValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where logValue does not contain
        defaultSpasArrLogFiltering("logValue.doesNotContain=" + UPDATED_LOG_VALUE, "logValue.doesNotContain=" + DEFAULT_LOG_VALUE);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel equals to
        defaultSpasArrLogFiltering("waterLevel.equals=" + DEFAULT_WATER_LEVEL, "waterLevel.equals=" + UPDATED_WATER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel in
        defaultSpasArrLogFiltering(
            "waterLevel.in=" + DEFAULT_WATER_LEVEL + "," + UPDATED_WATER_LEVEL,
            "waterLevel.in=" + UPDATED_WATER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel is not null
        defaultSpasArrLogFiltering("waterLevel.specified=true", "waterLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel is greater than or equal to
        defaultSpasArrLogFiltering(
            "waterLevel.greaterThanOrEqual=" + DEFAULT_WATER_LEVEL,
            "waterLevel.greaterThanOrEqual=" + UPDATED_WATER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel is less than or equal to
        defaultSpasArrLogFiltering(
            "waterLevel.lessThanOrEqual=" + DEFAULT_WATER_LEVEL,
            "waterLevel.lessThanOrEqual=" + SMALLER_WATER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel is less than
        defaultSpasArrLogFiltering("waterLevel.lessThan=" + UPDATED_WATER_LEVEL, "waterLevel.lessThan=" + DEFAULT_WATER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByWaterLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where waterLevel is greater than
        defaultSpasArrLogFiltering("waterLevel.greaterThan=" + SMALLER_WATER_LEVEL, "waterLevel.greaterThan=" + DEFAULT_WATER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel equals to
        defaultSpasArrLogFiltering("batteryLevel.equals=" + DEFAULT_BATTERY_LEVEL, "batteryLevel.equals=" + UPDATED_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel in
        defaultSpasArrLogFiltering(
            "batteryLevel.in=" + DEFAULT_BATTERY_LEVEL + "," + UPDATED_BATTERY_LEVEL,
            "batteryLevel.in=" + UPDATED_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel is not null
        defaultSpasArrLogFiltering("batteryLevel.specified=true", "batteryLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel is greater than or equal to
        defaultSpasArrLogFiltering(
            "batteryLevel.greaterThanOrEqual=" + DEFAULT_BATTERY_LEVEL,
            "batteryLevel.greaterThanOrEqual=" + UPDATED_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel is less than or equal to
        defaultSpasArrLogFiltering(
            "batteryLevel.lessThanOrEqual=" + DEFAULT_BATTERY_LEVEL,
            "batteryLevel.lessThanOrEqual=" + SMALLER_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel is less than
        defaultSpasArrLogFiltering("batteryLevel.lessThan=" + UPDATED_BATTERY_LEVEL, "batteryLevel.lessThan=" + DEFAULT_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByBatteryLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where batteryLevel is greater than
        defaultSpasArrLogFiltering(
            "batteryLevel.greaterThan=" + SMALLER_BATTERY_LEVEL,
            "batteryLevel.greaterThan=" + DEFAULT_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel equals to
        defaultSpasArrLogFiltering("rainLevel.equals=" + DEFAULT_RAIN_LEVEL, "rainLevel.equals=" + UPDATED_RAIN_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel in
        defaultSpasArrLogFiltering("rainLevel.in=" + DEFAULT_RAIN_LEVEL + "," + UPDATED_RAIN_LEVEL, "rainLevel.in=" + UPDATED_RAIN_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel is not null
        defaultSpasArrLogFiltering("rainLevel.specified=true", "rainLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel is greater than or equal to
        defaultSpasArrLogFiltering(
            "rainLevel.greaterThanOrEqual=" + DEFAULT_RAIN_LEVEL,
            "rainLevel.greaterThanOrEqual=" + UPDATED_RAIN_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel is less than or equal to
        defaultSpasArrLogFiltering("rainLevel.lessThanOrEqual=" + DEFAULT_RAIN_LEVEL, "rainLevel.lessThanOrEqual=" + SMALLER_RAIN_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel is less than
        defaultSpasArrLogFiltering("rainLevel.lessThan=" + UPDATED_RAIN_LEVEL, "rainLevel.lessThan=" + DEFAULT_RAIN_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsByRainLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        // Get all the spasArrLogList where rainLevel is greater than
        defaultSpasArrLogFiltering("rainLevel.greaterThan=" + SMALLER_RAIN_LEVEL, "rainLevel.greaterThan=" + DEFAULT_RAIN_LEVEL);
    }

    @Test
    @Transactional
    void getAllSpasArrLogsBySpasArrInstallIsEqualToSomething() throws Exception {
        SpasArrInstall spasArrInstall;
        if (TestUtil.findAll(em, SpasArrInstall.class).isEmpty()) {
            spasArrLogRepository.saveAndFlush(spasArrLog);
            spasArrInstall = SpasArrInstallResourceIT.createEntity();
        } else {
            spasArrInstall = TestUtil.findAll(em, SpasArrInstall.class).get(0);
        }
        em.persist(spasArrInstall);
        em.flush();
        spasArrLog.setSpasArrInstall(spasArrInstall);
        spasArrLogRepository.saveAndFlush(spasArrLog);
        Long spasArrInstallId = spasArrInstall.getId();
        // Get all the spasArrLogList where spasArrInstall equals to spasArrInstallId
        defaultSpasArrLogShouldBeFound("spasArrInstallId.equals=" + spasArrInstallId);

        // Get all the spasArrLogList where spasArrInstall equals to (spasArrInstallId + 1)
        defaultSpasArrLogShouldNotBeFound("spasArrInstallId.equals=" + (spasArrInstallId + 1));
    }

    private void defaultSpasArrLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSpasArrLogShouldBeFound(shouldBeFound);
        defaultSpasArrLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpasArrLogShouldBeFound(String filter) throws Exception {
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spasArrLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeLog").value(hasItem(sameInstant(DEFAULT_TIME_LOG))))
            .andExpect(jsonPath("$.[*].timeRetrieve").value(hasItem(sameInstant(DEFAULT_TIME_RETRIEVE))))
            .andExpect(jsonPath("$.[*].logValue").value(hasItem(DEFAULT_LOG_VALUE)))
            .andExpect(jsonPath("$.[*].waterLevel").value(hasItem(DEFAULT_WATER_LEVEL)))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL)))
            .andExpect(jsonPath("$.[*].rainLevel").value(hasItem(DEFAULT_RAIN_LEVEL)));

        // Check, that the count call also returns 1
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpasArrLogShouldNotBeFound(String filter) throws Exception {
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpasArrLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpasArrLog() throws Exception {
        // Get the spasArrLog
        restSpasArrLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpasArrLog() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrLog
        SpasArrLog updatedSpasArrLog = spasArrLogRepository.findById(spasArrLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpasArrLog are not directly saved in db
        em.detach(updatedSpasArrLog);
        updatedSpasArrLog
            .timeLog(UPDATED_TIME_LOG)
            .timeRetrieve(UPDATED_TIME_RETRIEVE)
            .logValue(UPDATED_LOG_VALUE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .batteryLevel(UPDATED_BATTERY_LEVEL)
            .rainLevel(UPDATED_RAIN_LEVEL);
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(updatedSpasArrLog);

        restSpasArrLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spasArrLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpasArrLogToMatchAllProperties(updatedSpasArrLog);
    }

    @Test
    @Transactional
    void putNonExistingSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spasArrLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasArrLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasArrLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpasArrLogWithPatch() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrLog using partial update
        SpasArrLog partialUpdatedSpasArrLog = new SpasArrLog();
        partialUpdatedSpasArrLog.setId(spasArrLog.getId());

        partialUpdatedSpasArrLog.timeLog(UPDATED_TIME_LOG).logValue(UPDATED_LOG_VALUE);

        restSpasArrLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpasArrLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpasArrLog))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasArrLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpasArrLog, spasArrLog),
            getPersistedSpasArrLog(spasArrLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpasArrLogWithPatch() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spasArrLog using partial update
        SpasArrLog partialUpdatedSpasArrLog = new SpasArrLog();
        partialUpdatedSpasArrLog.setId(spasArrLog.getId());

        partialUpdatedSpasArrLog
            .timeLog(UPDATED_TIME_LOG)
            .timeRetrieve(UPDATED_TIME_RETRIEVE)
            .logValue(UPDATED_LOG_VALUE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .batteryLevel(UPDATED_BATTERY_LEVEL)
            .rainLevel(UPDATED_RAIN_LEVEL);

        restSpasArrLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpasArrLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpasArrLog))
            )
            .andExpect(status().isOk());

        // Validate the SpasArrLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasArrLogUpdatableFieldsEquals(partialUpdatedSpasArrLog, getPersistedSpasArrLog(partialUpdatedSpasArrLog));
    }

    @Test
    @Transactional
    void patchNonExistingSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spasArrLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spasArrLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spasArrLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpasArrLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spasArrLog.setId(longCount.incrementAndGet());

        // Create the SpasArrLog
        SpasArrLogDTO spasArrLogDTO = spasArrLogMapper.toDto(spasArrLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasArrLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(spasArrLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpasArrLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpasArrLog() throws Exception {
        // Initialize the database
        insertedSpasArrLog = spasArrLogRepository.saveAndFlush(spasArrLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the spasArrLog
        restSpasArrLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, spasArrLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return spasArrLogRepository.count();
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

    protected SpasArrLog getPersistedSpasArrLog(SpasArrLog spasArrLog) {
        return spasArrLogRepository.findById(spasArrLog.getId()).orElseThrow();
    }

    protected void assertPersistedSpasArrLogToMatchAllProperties(SpasArrLog expectedSpasArrLog) {
        assertSpasArrLogAllPropertiesEquals(expectedSpasArrLog, getPersistedSpasArrLog(expectedSpasArrLog));
    }

    protected void assertPersistedSpasArrLogToMatchUpdatableProperties(SpasArrLog expectedSpasArrLog) {
        assertSpasArrLogAllUpdatablePropertiesEquals(expectedSpasArrLog, getPersistedSpasArrLog(expectedSpasArrLog));
    }
}
