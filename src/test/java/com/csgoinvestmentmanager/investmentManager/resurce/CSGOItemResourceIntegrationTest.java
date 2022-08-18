package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.CSGOItemServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.validation.constraints.Null;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = CSGOItemResource.class)
@ExtendWith(MockitoExtension.class)
class CSGOItemResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CSGOItemServiceImplementation csgoItemService;

    private Optional<CSGOItem> testItem = Optional.of(new CSGOItem(
            "Gamma%20Case",
            "ImageUrl"
    ));
    long id = 1;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCSGOItems() {

    }

    @Test
    void getCSGOItem() throws Exception {
        //given
        //when
        when(csgoItemService
                .get(id))
                .thenReturn(testItem.orElse(null));
        //then
       MvcResult mvcResult = mockMvc.perform(get("/csgoitem/{id}","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("csgo Item retrived"))
                .andReturn();
        verify(csgoItemService).get(id);
    }

    @Test
    void saveServer() {
    }

    @Test
    void refreshCSGOItem() {
    }

    @Test
    void refreshAllCSGOItem() {
    }

    @Test
    void updateCSGOItem() {
    }

    @Test
    void deleteServer() {
    }
}