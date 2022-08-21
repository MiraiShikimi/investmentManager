package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.CSGOItemServiceImplementation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.validation.constraints.Null;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    private Optional<CSGOItem> testItem = Optional.of(new CSGOItem(
            "Gamma%20Case",
            "ImageUrl"
    ));
    private List<CSGOItem> testList = new ArrayList<CSGOItem>();
    long id = 1;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCSGOItems() throws Exception {

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
    void getCSGOItem() throws Exception {
        //given
        testList.add(testItem.get());
        //when
        when(csgoItemService
                .list(50))
                .thenReturn(testList);
        //then
       MvcResult mvcResult = mockMvc.perform(get("/csgoitem/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("csgo items retrieved"))
                .andReturn();
        verify(csgoItemService).list(50);
    }

    @Test
    void saveCSGOItem() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        //when
        when(csgoItemService
                .create(testItem.get()))
                .thenReturn(testItem.get());

        String requestJson=ow.writeValueAsString(testItem.get());

        //then
        MvcResult mvcResult = mockMvc.perform(post("/csgoitem/save")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("CSGO Item created"))
                .andReturn();
        verify(csgoItemService).create(testItem.get());

    }

    @Test
    void refreshCSGOItem() throws Exception {

        //given
        //when
        when(csgoItemService
                .refresh(id))
                .thenReturn(testItem.get());
        //then
        MvcResult mvcResult = mockMvc.perform(get("/csgoitem/updateprice/{id}",id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("CSGO Item price updated"))
                .andExpect(jsonPath("$.statusCode").value("201"))
                .andReturn();
        verify(csgoItemService).refresh(id);
    }


    @Test
    void refreshAllCSGOItem() throws Exception {

        //given
        //when
        when(csgoItemService
                .refreshAllPrices())
                .thenReturn("done");
        //then
        MvcResult mvcResult = mockMvc.perform(put("/csgoitem/updateprice/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("all prices have been updated"))
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andReturn();
        verify(csgoItemService).refreshAllPrices();
    }



    @Test
    void updateCSGOItem() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        //when
        when(csgoItemService
                .update(testItem.get()))
                .thenReturn(testItem.get());

        String requestJson=ow.writeValueAsString(testItem.get());

        //then
        MvcResult mvcResult = mockMvc.perform(put("/csgoitem/save")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("CSGO Item created"))
                .andReturn();
        verify(csgoItemService).update(testItem.get());

    }



    @Test
    void deleteServer() throws Exception {
        //given
        testList.add(testItem.get());
        //when
        when(csgoItemService
                .delete(id))
                .thenReturn(Boolean.TRUE);
        //then
        MvcResult mvcResult = mockMvc.perform(delete("/csgoitem/delete/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("item deleted"))
                .andReturn();
        verify(csgoItemService).delete(id);
    }

}
