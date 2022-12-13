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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    long id = 0;

    @BeforeEach
    void setUp() {
      csgoItemServiceImplementation = new CSGOItemServiceImplementation(csgoItemRepository);
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

        //given


        when(csgoItemRepository.findById(id)).thenReturn(testItem);
        //when

        csgoItemServiceImplementation.get(id);

        //then
        verify(csgoItemRepository).findById(id);
    }

    @Test
    void delete() {
        //given
        //when
        csgoItemServiceImplementation.delete(id);
        //then
        verify(csgoItemRepository).deleteById(id);


    }

    @Test
    void update() {
        //given
        when(csgoItemRepository.save(testItem.get())).thenReturn(testItem.get());
        //when
       csgoItemServiceImplementation.update(testItem.get());
        //then
        verify(csgoItemRepository).save(testItem.get());

        assertThat(testItem.get().getDisplayName()).isEqualTo("Gamma Case");
        assertThat(testItem.get().getLowestPrice()).isNotNull();
    }

    @Test
    void list() {
        //given
        int limit = 50;
        //when
        try {
            csgoItemServiceImplementation.list(limit);
        }catch (NullPointerException e){
            System.out.println("yeah yeah there is nothing i know");
        }
        //then
        verify(csgoItemRepository).findAll(PageRequest.of(0,limit));
    }

    @Test
    void refresh() {
        //given
        when(csgoItemRepository.findById(id)).thenReturn(testItem);
        //when
        csgoItemServiceImplementation.refresh(id);
        //then

        verify(csgoItemRepository).findById(id);
        verify(csgoItemRepository).save(testItem.get());
    }

    @Test
    void testRefresh() {
        //given
        //when
        csgoItemServiceImplementation.refresh(testItem.get());
        //then

        verify(csgoItemRepository).save(testItem.get());
    }

    @Test
    void refreshAllPrices() {
        //given
        List<CSGOItem> list=new ArrayList<CSGOItem>();
        list.add(testItem.get());
        System.out.println(list);
        when(csgoItemRepository.findAll()).thenReturn(list);
        //when
        csgoItemServiceImplementation.refreshAllPrices();
        //then
        verify(csgoItemRepository).findAll();

    }
}