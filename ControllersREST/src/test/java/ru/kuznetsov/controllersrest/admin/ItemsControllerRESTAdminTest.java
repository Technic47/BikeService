package ru.kuznetsov.controllersrest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ItemsControllerRESTAdminTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("pavel")
    void getCreatedItems() throws Exception {
        mockMvc.perform(get("/api/admin/items/createdBy/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.documents", hasSize(2)))
                .andExpect(jsonPath("$.fasteners", hasSize(1)))
                .andExpect(jsonPath("$.manufacturers", hasSize(1)))
                .andExpect(jsonPath("$.consumables", hasSize(1)))
                .andExpect(jsonPath("$.tools", hasSize(1)))
                .andExpect(jsonPath("$.parts", hasSize(2)))
                .andExpect(jsonPath("$.bikes", hasSize(0)));
    }

    @Test
    @WithUserDetails("test")
    void getCreatedItemsUser() throws Exception {
        mockMvc.perform(get("/api/admin/items/createdBy/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("pavel")
    void getDocumentsList() throws Exception {
        mockMvc.perform(get("/api/admin/items/documents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    @WithUserDetails("pavel")
    void getFastenersList() throws Exception {
        mockMvc.perform(get("/api/admin/items/fasteners"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getManufacturersList() throws Exception {
        mockMvc.perform(get("/api/admin/items/manufacturers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getConsumablesList() throws Exception {
        mockMvc.perform(get("/api/admin/items/consumables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getToolsList() throws Exception {
        mockMvc.perform(get("/api/admin/items/tools"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails("pavel")
    void getPartsList() throws Exception {
        mockMvc.perform(get("/api/admin/items/parts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    @WithUserDetails("pavel")
    void getBikesList() throws Exception {
        mockMvc.perform(get("/api/admin/items/bikes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithUserDetails("pavel")
    void getDocument() throws Exception {
        mockMvc.perform(get("/api/admin/items/documents/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getFastener() throws Exception {
        mockMvc.perform(get("/api/admin/items/fasteners/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getManufacturer() throws Exception {
        mockMvc.perform(get("/api/admin/items/manufacturers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getConsumable() throws Exception {
        mockMvc.perform(get("/api/admin/items/consumables/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getTool() throws Exception {
        mockMvc.perform(get("/api/admin/items/tools/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getPart() throws Exception {
        mockMvc.perform(get("/api/admin/items/parts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithUserDetails("pavel")
    void getBike() throws Exception {
        mockMvc.perform(get("/api/admin/items/bikes/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}