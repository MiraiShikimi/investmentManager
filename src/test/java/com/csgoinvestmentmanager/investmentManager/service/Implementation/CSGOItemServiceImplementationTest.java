package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.repository.CSGOItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.Null;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CSGOItemServiceImplementationTest {

    @Mock
    private CSGOItemRepository csgoItemRepository;
    private CSGOItemServiceImplementation csgoItemServiceImplementation;
    Optional<CSGOItem> testItem = Optional.of(new CSGOItem(
            "Gamma%20Case",
            "ImageUrl"
    ));

    @BeforeEach
    void setUp() {
        csgoItemServiceImplementation = new CSGOItemServiceImplementation(csgoItemRepository);

    }

    @Test
    void get() {

        //given
        long id = 0;

        when(csgoItemRepository.findById(id)).thenReturn(testItem);
        //when

        csgoItemServiceImplementation.get(id);

        //then
        verify(csgoItemRepository).findById(id);
    }


    @Test
    void create() {
        //given
        CSGOItem csgoItem = new CSGOItem(
                "Gamma%20Case",
                "image url"
        );

        //when
        csgoItemServiceImplementation.create(csgoItem);

        //then
        ArgumentCaptor<CSGOItem> csgoItemArgumentCaptor =
                ArgumentCaptor.forClass(CSGOItem.class);

        verify(csgoItemRepository).save(csgoItemArgumentCaptor.capture());

        CSGOItem capturedCSGOItem = csgoItemArgumentCaptor.getValue();

        assertThat(capturedCSGOItem).isEqualTo(csgoItem);

    }

    @Test
    void testGet() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }

    @Test
    void list() {
        //given
        int limit = 50;
        //when
        try {
            csgoItemServiceImplementation.list(limit);
        }catch (NullPointerException e){
            System.out.println("yeah yeah thre is knothing i know");
        }
        //then
        verify(csgoItemRepository).findAll(PageRequest.of(0,limit));
    }

    @Test
    void refresh() {
    }

    @Test
    void testRefresh() {
    }

    @Test
    void refreshAllPrices() {
    }
}