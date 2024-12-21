package com.zahid.socks_api.services;

import com.zahid.socks_api.entity.SocksBatch;
import com.zahid.socks_api.exceptions.LackOfSocksException;
import com.zahid.socks_api.exceptions.SocksDataException;
import com.zahid.socks_api.repositories.SocksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocksServiceTests {
    @Mock
    private SocksRepository socksRepository;
    @InjectMocks
    private SocksService socksService;
    @Test
    void delete_EqualAmount() {
        SocksBatch socks = new SocksBatch("pink", 50, 10);
        when(socksRepository.findSocksBatchByColorAndCotton("pink", 50)).thenReturn(Optional.of(socks));
        socksService.delete(new SocksBatch("pink", 50, 10));
        verify(socksRepository, times(1)).delete(socks);
        verify(socksRepository, never()).save(any(SocksBatch.class));
    }

    @Test
    void delete_LessThanAvailable() {
        SocksBatch socks = new SocksBatch("blue", 80, 20);
        when(socksRepository.findSocksBatchByColorAndCotton("blue", 80)).thenReturn(Optional.of(socks));
        socksService.delete(new SocksBatch("blue", 80, 5));
        verify(socksRepository, never()).delete(any(SocksBatch.class));
        verify(socksRepository, times(1)).save(argThat(s -> s.getColor().equals("blue") && s.getCotton() == 80 && s.getQuantity() == 15));
    }

    @Test
    void delete_NonExistentSocksType() {
        when(socksRepository.findSocksBatchByColorAndCotton("green", 75)).thenReturn(Optional.empty());
        assertThrows(LackOfSocksException.class,
                () -> socksService.delete(new SocksBatch("green", 75, 5)));
        verify(socksRepository, never()).delete(any(SocksBatch.class));
        verify(socksRepository, never()).save(any(SocksBatch.class));
    }

    @Test
    void delete_MoreThanAvailable() {
        SocksBatch socks = new SocksBatch("black", 60, 12);
        when(socksRepository.findSocksBatchByColorAndCotton("black", 60)).thenReturn(Optional.of(socks));
        assertThrows(LackOfSocksException.class,
                () -> socksService.delete(new SocksBatch("black", 60, 15)));
        verify(socksRepository, never()).delete(any(SocksBatch.class));
        verify(socksRepository, never()).save(any(SocksBatch.class));
    }

    @Test
    void change_updatesExistingSocksBatch() {
        SocksBatch existingSocks = new SocksBatch(1, "red", 50, 10);
        SocksBatch newValues = new SocksBatch(1,"blue", 80, 5);
        when(socksRepository.findById(1)).thenReturn(Optional.of(existingSocks));
        when(socksRepository.findSocksBatchByColorAndCotton("blue", 80))
                .thenReturn(Optional.empty());
        socksService.change(newValues,1);
        verify(socksRepository, times(1)).findById(1);
        verify(socksRepository, times(1))
                .save(argThat(s -> s.getColor().equals("blue") && s.getCotton() == 80 && s.getQuantity() == 5));
        verify(socksRepository, never()).delete(any(SocksBatch.class));
    }

    @Test
    void change_MergesWithExistingSocksBatch() {
        SocksBatch existingSocks = new SocksBatch(2, "green", 75, 15);
        SocksBatch newValues = new SocksBatch("blue", 88, 5);
        SocksBatch sameFeaturesSocks = new SocksBatch( "blue", 88, 30);
        when(socksRepository.findById(2)).thenReturn(Optional.of(existingSocks));
        when(socksRepository.findSocksBatchByColorAndCotton("blue", 88))
                .thenReturn(Optional.of(sameFeaturesSocks));
        socksService.change(newValues, 2);
        verify(socksRepository, times(1)).findById(2);
        verify(socksRepository, times(1)).delete(sameFeaturesSocks);
        verify(socksRepository, times(1)).save(any(SocksBatch.class));
        assertEquals(35, existingSocks.getQuantity());
    }

    @Test
    void change_NonExistentSocksBatch_ThrowsException() {
        SocksBatch newValues = new SocksBatch("blue", 80, 5);
        when(socksRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(SocksDataException.class, () -> socksService.change(newValues, 1));
        verify(socksRepository, times(1)).findById(1);
        verify(socksRepository, never()).save(any(SocksBatch.class));
        verify(socksRepository, never()).delete(any(SocksBatch.class));
    }

    @Test
    void change_NotAllFields() {
        SocksBatch existingSocks = new SocksBatch(3, "red", 50, 10);
        SocksBatch newValues = new SocksBatch(null, 0, 5);
        when(socksRepository.findById(3)).thenReturn(Optional.of(existingSocks));
        when(socksRepository.findSocksBatchByColorAndCotton(null, 0)).thenReturn(Optional.empty());
        socksService.change(newValues, 3);
        verify(socksRepository, times(1)).save(any(SocksBatch.class));
        assertEquals("red", existingSocks.getColor());
        assertEquals(50, existingSocks.getCotton());
        assertEquals(5, existingSocks.getQuantity());
    }

    @Test
    void searchWithFilter_WithoutFilters() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        List<SocksBatch> result = socksService.searchWithFilter(null, null, null);
        assertEquals(initialSocks, result);
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void searchWithFilter_MinCotton() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        List<SocksBatch> result = socksService.searchWithFilter(null, 60, null);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(x -> x.getCotton() >= 60));
        verify(socksRepository, times(1)).findAll();
    }


    @Test
    void searchWithFilter_MaxCotton() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        List<SocksBatch> result = socksService.searchWithFilter(null, null, 60);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(x -> x.getCotton() <= 60));
        verify(socksRepository, times(1)).findAll();
    }


    @Test
    void searchWithFilter_MinAndMaxCotton() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        List<SocksBatch> result = socksService.searchWithFilter(null, 55, 65);
        assertEquals(1, result.size());
        assertEquals(60, result.get(0).getCotton());
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void searchWithFilter_ColorSort() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        List<SocksBatch> result = socksService.searchWithFilter("color", null, null);
        assertEquals(3, result.size());
        assertEquals("blue", result.get(0).getColor());
        assertEquals("green", result.get(1).getColor());
        assertEquals("red", result.get(2).getColor());
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void searchWithFilter_CottonSort() {
        List<SocksBatch> initialSocks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "green", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);

        List<SocksBatch> result = socksService.searchWithFilter("cotton", null, null);

        assertEquals(3, result.size());
        assertEquals(50, result.get(0).getCotton());
        assertEquals(60, result.get(1).getCotton());
        assertEquals(70, result.get(2).getCotton());
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void searchWithFilter_invalidSortCriteria() {
        List<SocksBatch> initialSocks = List.of(
                new SocksBatch(1, "red", 50, 10)
        );
        when(socksRepository.findAll()).thenReturn(initialSocks);
        assertThrows(RuntimeException.class,
                () -> socksService.searchWithFilter("zebra", null, null));
        verify(socksRepository, times(1)).findAll();
    }


    @Test
    void count_WithoutFilters() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 50, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count(null, null, null, null);
        assertEquals(45, result);
        verify(socksRepository, times(1)).findAll();
    }


    @Test
    void count_Color() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 50, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count("red", null, null, null);
        assertEquals(30, result);
        verify(socksRepository, times(1)).findAll();
    }


    @Test
    void count_CottonPercentage() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 50, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count(null, null, 50, null);
        assertEquals(30, result);
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void count_MinCotton() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count(null, 60, null, null);
        assertEquals(35, result);
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void count_MaxCotton() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count(null, null, null, 60);
        assertEquals(25, result);
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void count_MinAndMaxCotton() {
        List<SocksBatch> socks = Arrays.asList(
                new SocksBatch(1, "red", 50, 10),
                new SocksBatch(2, "blue", 60, 15),
                new SocksBatch(3, "red", 70, 20)
        );
        when(socksRepository.findAll()).thenReturn(socks);
        int result = socksService.count(null, 55, null, 65);
        assertEquals(15, result);
        verify(socksRepository, times(1)).findAll();
    }

    @Test
    void count_WhenBothNumberAndRange_ThrowsException() {
        assertThrows(SocksDataException.class, () -> socksService.count(null, 50, 50, 60));
    }
}
