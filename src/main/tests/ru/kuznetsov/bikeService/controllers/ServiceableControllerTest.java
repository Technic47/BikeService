package ru.kuznetsov.bikeService.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuznetsov.bikeService.services.ManufacturerService;
import ru.kuznetsov.bikeService.services.PartService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("pavel")
class ServiceableControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private PartService partService;

    @Test
    void show() throws Exception {
        Long manufactureIndex = this.partService.show(1L).getManufacturer();
        String manufactureName = manufacturerService.show(manufactureIndex).getName();

        this.mockMvc.perform(get("/parts/1"))
                .andDo(print())
                .andExpect(model().attribute("documents", aMapWithSize(1)))
                .andExpect(model().attribute("fasteners", aMapWithSize(1)))
                .andExpect(model().attribute("tools", aMapWithSize(1)))
                .andExpect(model().attribute("consumables", aMapWithSize(1)))
                .andExpect(model().attribute("parts", aMapWithSize(1)))
                .andExpect(model().attribute("manufacture", manufactureName))
                .andExpect(content().string(containsString("Manufacturer - testManufacture")))
                .andExpect(content().string(containsString("Model - testModel")))
                .andExpect(content().string(containsString("testManufacture testModel")))
                .andExpect(content().string(containsString("testPart1 testModel")))
                .andExpect(content().string(containsString("testDoc1 1")))
                .andExpect(content().string(containsString("testTool1 1")))
                .andExpect(content().string(containsString("testFast1 1")))
                .andExpect(content().string(containsString("testCons1 1")));
    }

    @Test
    void edit() throws Exception {
        this.mockMvc.perform(get("/parts/1/edit"))
                .andDo(print())
                .andExpect(model().attribute("allDocuments",  hasSize(5)))
                .andExpect(model().attribute("allFasteners",  hasSize(1)))
                .andExpect(model().attribute("allTools",  hasSize(1)))
                .andExpect(model().attribute("allConsumables",  hasSize(1)))
                .andExpect(model().attribute("allParts",  hasSize(5)))
                .andExpect(model().attribute("documents", aMapWithSize(1)))
                .andExpect(model().attribute("fasteners", aMapWithSize(1)))
                .andExpect(model().attribute("tools", aMapWithSize(1)))
                .andExpect(model().attribute("consumables", aMapWithSize(1)))
                .andExpect(model().attribute("parts", aMapWithSize(1)))
                .andExpect(model().attribute("type", "Serviceable"))
                .andExpect(view().name("/edit/editPart"))
                .andExpect(xpath("//div/div/form[@action='/parts/1/update']").exists())
                .andExpect(xpath("//div/div/form/div/div/div/select[@id='allParts']/option").nodeCount(4))
                .andExpect(xpath("//div/div/form/div/div/div/select[@id='documents']/option").nodeCount(5))
                .andExpect(xpath("//div/div/form/div/div/div/select[@id='tools']/option").nodeCount(1))
                .andExpect(xpath("//div/div/form/div/div/div/select[@id='fasteners']/option").nodeCount(1))
                .andExpect(xpath("//div/div/form/div/div/div/select[@id='consumables']/option").nodeCount(1))

                .andExpect(xpath("//div/div/div/div/table/tbody/tr/td/a[@href='/parts/1']").exists())
                .andExpect(xpath("//div/div/div/div/table/tbody/tr/td/a[@href='/documents/1']").exists())
                .andExpect(xpath("//div/div/div/div/table/tbody/tr/td/a[@href='/fasteners/1']").exists())
                .andExpect(xpath("//div/div/div/div/table/tbody/tr/td/a[@href='/consumables/1']").exists())
                .andExpect(xpath("//div/div/div/div/table/tbody/tr/td/a[@href='/tools/1']").exists());
    }

    @Test
    void updateServiceList() {
    }

    @Test
    void addItemAttributesNew() {
    }
}