package com.sipdasrh.awas.web.rest;

import static com.sipdasrh.awas.domain.SpasAsserts.*;
import static com.sipdasrh.awas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipdasrh.awas.IntegrationTest;
import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.repository.SpasRepository;
import com.sipdasrh.awas.service.dto.SpasDTO;
import com.sipdasrh.awas.service.mapper.SpasMapper;
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
 * Integration tests for the {@link SpasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpasResourceIT {

    private static final String DEFAULT_NAMA_SPAS = "AAAAAAAAAA";
    private static final String UPDATED_NAMA_SPAS = "BBBBBBBBBB";

    private static final String DEFAULT_NAMA_MANUFAKTUR = "AAAAAAAAAA";
    private static final String UPDATED_NAMA_MANUFAKTUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIPE_SPAS = 1;
    private static final Integer UPDATED_TIPE_SPAS = 2;
    private static final Integer SMALLER_TIPE_SPAS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/spas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpasRepository spasRepository;

    @Autowired
    private SpasMapper spasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpasMockMvc;

    private Spas spas;

    private Spas insertedSpas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spas createEntity() {
        return new Spas().namaSpas(DEFAULT_NAMA_SPAS).namaManufaktur(DEFAULT_NAMA_MANUFAKTUR).tipeSpas(DEFAULT_TIPE_SPAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spas createUpdatedEntity() {
        return new Spas().namaSpas(UPDATED_NAMA_SPAS).namaManufaktur(UPDATED_NAMA_MANUFAKTUR).tipeSpas(UPDATED_TIPE_SPAS);
    }

    @BeforeEach
    void initTest() {
        spas = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpas != null) {
            spasRepository.delete(insertedSpas);
            insertedSpas = null;
        }
    }

    @Test
    @Transactional
    void createSpas() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);
        var returnedSpasDTO = om.readValue(
            restSpasMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpasDTO.class
        );

        // Validate the Spas in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpas = spasMapper.toEntity(returnedSpasDTO);
        assertSpasUpdatableFieldsEquals(returnedSpas, getPersistedSpas(returnedSpas));

        insertedSpas = returnedSpas;
    }

    @Test
    @Transactional
    void createSpasWithExistingId() throws Exception {
        // Create the Spas with an existing ID
        spas.setId(1L);
        SpasDTO spasDTO = spasMapper.toDto(spas);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpas() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList
        restSpasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spas.getId().intValue())))
            .andExpect(jsonPath("$.[*].namaSpas").value(hasItem(DEFAULT_NAMA_SPAS)))
            .andExpect(jsonPath("$.[*].namaManufaktur").value(hasItem(DEFAULT_NAMA_MANUFAKTUR)))
            .andExpect(jsonPath("$.[*].tipeSpas").value(hasItem(DEFAULT_TIPE_SPAS)));
    }

    @Test
    @Transactional
    void getSpas() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get the spas
        restSpasMockMvc
            .perform(get(ENTITY_API_URL_ID, spas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spas.getId().intValue()))
            .andExpect(jsonPath("$.namaSpas").value(DEFAULT_NAMA_SPAS))
            .andExpect(jsonPath("$.namaManufaktur").value(DEFAULT_NAMA_MANUFAKTUR))
            .andExpect(jsonPath("$.tipeSpas").value(DEFAULT_TIPE_SPAS));
    }

    @Test
    @Transactional
    void getSpasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        Long id = spas.getId();

        defaultSpasFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSpasFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSpasFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpasByNamaSpasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaSpas equals to
        defaultSpasFiltering("namaSpas.equals=" + DEFAULT_NAMA_SPAS, "namaSpas.equals=" + UPDATED_NAMA_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByNamaSpasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaSpas in
        defaultSpasFiltering("namaSpas.in=" + DEFAULT_NAMA_SPAS + "," + UPDATED_NAMA_SPAS, "namaSpas.in=" + UPDATED_NAMA_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByNamaSpasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaSpas is not null
        defaultSpasFiltering("namaSpas.specified=true", "namaSpas.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasByNamaSpasContainsSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaSpas contains
        defaultSpasFiltering("namaSpas.contains=" + DEFAULT_NAMA_SPAS, "namaSpas.contains=" + UPDATED_NAMA_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByNamaSpasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaSpas does not contain
        defaultSpasFiltering("namaSpas.doesNotContain=" + UPDATED_NAMA_SPAS, "namaSpas.doesNotContain=" + DEFAULT_NAMA_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByNamaManufakturIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaManufaktur equals to
        defaultSpasFiltering("namaManufaktur.equals=" + DEFAULT_NAMA_MANUFAKTUR, "namaManufaktur.equals=" + UPDATED_NAMA_MANUFAKTUR);
    }

    @Test
    @Transactional
    void getAllSpasByNamaManufakturIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaManufaktur in
        defaultSpasFiltering(
            "namaManufaktur.in=" + DEFAULT_NAMA_MANUFAKTUR + "," + UPDATED_NAMA_MANUFAKTUR,
            "namaManufaktur.in=" + UPDATED_NAMA_MANUFAKTUR
        );
    }

    @Test
    @Transactional
    void getAllSpasByNamaManufakturIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaManufaktur is not null
        defaultSpasFiltering("namaManufaktur.specified=true", "namaManufaktur.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasByNamaManufakturContainsSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaManufaktur contains
        defaultSpasFiltering("namaManufaktur.contains=" + DEFAULT_NAMA_MANUFAKTUR, "namaManufaktur.contains=" + UPDATED_NAMA_MANUFAKTUR);
    }

    @Test
    @Transactional
    void getAllSpasByNamaManufakturNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where namaManufaktur does not contain
        defaultSpasFiltering(
            "namaManufaktur.doesNotContain=" + UPDATED_NAMA_MANUFAKTUR,
            "namaManufaktur.doesNotContain=" + DEFAULT_NAMA_MANUFAKTUR
        );
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas equals to
        defaultSpasFiltering("tipeSpas.equals=" + DEFAULT_TIPE_SPAS, "tipeSpas.equals=" + UPDATED_TIPE_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas in
        defaultSpasFiltering("tipeSpas.in=" + DEFAULT_TIPE_SPAS + "," + UPDATED_TIPE_SPAS, "tipeSpas.in=" + UPDATED_TIPE_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas is not null
        defaultSpasFiltering("tipeSpas.specified=true", "tipeSpas.specified=false");
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas is greater than or equal to
        defaultSpasFiltering("tipeSpas.greaterThanOrEqual=" + DEFAULT_TIPE_SPAS, "tipeSpas.greaterThanOrEqual=" + UPDATED_TIPE_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas is less than or equal to
        defaultSpasFiltering("tipeSpas.lessThanOrEqual=" + DEFAULT_TIPE_SPAS, "tipeSpas.lessThanOrEqual=" + SMALLER_TIPE_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas is less than
        defaultSpasFiltering("tipeSpas.lessThan=" + UPDATED_TIPE_SPAS, "tipeSpas.lessThan=" + DEFAULT_TIPE_SPAS);
    }

    @Test
    @Transactional
    void getAllSpasByTipeSpasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        // Get all the spasList where tipeSpas is greater than
        defaultSpasFiltering("tipeSpas.greaterThan=" + SMALLER_TIPE_SPAS, "tipeSpas.greaterThan=" + DEFAULT_TIPE_SPAS);
    }

    private void defaultSpasFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSpasShouldBeFound(shouldBeFound);
        defaultSpasShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpasShouldBeFound(String filter) throws Exception {
        restSpasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spas.getId().intValue())))
            .andExpect(jsonPath("$.[*].namaSpas").value(hasItem(DEFAULT_NAMA_SPAS)))
            .andExpect(jsonPath("$.[*].namaManufaktur").value(hasItem(DEFAULT_NAMA_MANUFAKTUR)))
            .andExpect(jsonPath("$.[*].tipeSpas").value(hasItem(DEFAULT_TIPE_SPAS)));

        // Check, that the count call also returns 1
        restSpasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpasShouldNotBeFound(String filter) throws Exception {
        restSpasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpas() throws Exception {
        // Get the spas
        restSpasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpas() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spas
        Spas updatedSpas = spasRepository.findById(spas.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpas are not directly saved in db
        em.detach(updatedSpas);
        updatedSpas.namaSpas(UPDATED_NAMA_SPAS).namaManufaktur(UPDATED_NAMA_MANUFAKTUR).tipeSpas(UPDATED_TIPE_SPAS);
        SpasDTO spasDTO = spasMapper.toDto(updatedSpas);

        restSpasMockMvc
            .perform(put(ENTITY_API_URL_ID, spasDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasDTO)))
            .andExpect(status().isOk());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpasToMatchAllProperties(updatedSpas);
    }

    @Test
    @Transactional
    void putNonExistingSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(put(ENTITY_API_URL_ID, spasDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpasWithPatch() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spas using partial update
        Spas partialUpdatedSpas = new Spas();
        partialUpdatedSpas.setId(spas.getId());

        partialUpdatedSpas.namaSpas(UPDATED_NAMA_SPAS).namaManufaktur(UPDATED_NAMA_MANUFAKTUR).tipeSpas(UPDATED_TIPE_SPAS);

        restSpasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpas))
            )
            .andExpect(status().isOk());

        // Validate the Spas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSpas, spas), getPersistedSpas(spas));
    }

    @Test
    @Transactional
    void fullUpdateSpasWithPatch() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the spas using partial update
        Spas partialUpdatedSpas = new Spas();
        partialUpdatedSpas.setId(spas.getId());

        partialUpdatedSpas.namaSpas(UPDATED_NAMA_SPAS).namaManufaktur(UPDATED_NAMA_MANUFAKTUR).tipeSpas(UPDATED_TIPE_SPAS);

        restSpasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpas))
            )
            .andExpect(status().isOk());

        // Validate the Spas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpasUpdatableFieldsEquals(partialUpdatedSpas, getPersistedSpas(partialUpdatedSpas));
    }

    @Test
    @Transactional
    void patchNonExistingSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spasDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(spasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        spas.setId(longCount.incrementAndGet());

        // Create the Spas
        SpasDTO spasDTO = spasMapper.toDto(spas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(spasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpas() throws Exception {
        // Initialize the database
        insertedSpas = spasRepository.saveAndFlush(spas);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the spas
        restSpasMockMvc
            .perform(delete(ENTITY_API_URL_ID, spas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return spasRepository.count();
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

    protected Spas getPersistedSpas(Spas spas) {
        return spasRepository.findById(spas.getId()).orElseThrow();
    }

    protected void assertPersistedSpasToMatchAllProperties(Spas expectedSpas) {
        assertSpasAllPropertiesEquals(expectedSpas, getPersistedSpas(expectedSpas));
    }

    protected void assertPersistedSpasToMatchUpdatableProperties(Spas expectedSpas) {
        assertSpasAllUpdatablePropertiesEquals(expectedSpas, getPersistedSpas(expectedSpas));
    }
}
