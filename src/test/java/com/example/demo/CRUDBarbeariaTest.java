package com.example.demo;

import com.example.demo.DTOs.BarbeariaDTO;
import com.example.demo.services.BarbeariaService; 
import com.example.demo.models.Barbearia;
import com.example.demo.repositories.BarbeariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CRUDBarbeariaTest {

    @InjectMocks
    private BarbeariaService barbeariaService;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private BarbeariaDTO getSampleDTO() {
    return new BarbeariaDTO(
        null,                       
        "Barbearia X",                 
        "Rua A, 123",                   
        "00.000.000/0001-00",           
        List.of(1L, 2L),                
        "08:00 - 18:00",                
        10000.0                         
    );
}


    private Barbearia getSampleBarbearia(Long id) {
        Barbearia b = new Barbearia();
        b.setId(id);
        b.setNome("Barbearia X");
        b.setEndereco("Rua A, 123");
        b.setCnpj("00.000.000/0001-00");
        b.setHorarioFuncionamento("08:00 - 18:00");
        b.setFaturamento(10000.0);
        return b;
    }

    @Test
    void testCadastrarBarbearia() {
        BarbeariaDTO dto = getSampleDTO();
        Barbearia expected = getSampleBarbearia(1L);

        when(barbeariaRepository.save(any(Barbearia.class))).thenReturn(expected);

        Barbearia result = barbeariaService.cadastrarBarbearia(dto);

        assertNotNull(result);
        assertEquals("Barbearia X", result.getNome());
        verify(barbeariaRepository, times(1)).save(any(Barbearia.class));
    }

    @Test
    void testAtualizarBarbearia() {
        BarbeariaDTO dto = getSampleDTO();
        Barbearia existing = getSampleBarbearia(1L);

        when(barbeariaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(barbeariaRepository.save(any(Barbearia.class))).thenReturn(existing);

        Barbearia result = barbeariaService.atualizarBarbearia(1L, dto);

        assertNotNull(result);
        assertEquals("Barbearia X", result.getNome());
        verify(barbeariaRepository, times(1)).findById(1L);
        verify(barbeariaRepository, times(1)).save(existing);
    }

    @Test
    void testExcluirBarbearia() {
        Barbearia existing = getSampleBarbearia(1L);

        when(barbeariaRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(barbeariaRepository).delete(existing);

        assertDoesNotThrow(() -> barbeariaService.excluirBarbearia(1L));
        verify(barbeariaRepository).delete(existing);
    }

    @Test
    void testBuscarBarbearia() {
        Barbearia expected = getSampleBarbearia(1L);

        when(barbeariaRepository.findById(1L)).thenReturn(Optional.of(expected));

        Barbearia result = barbeariaService.buscarBarbearia(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(barbeariaRepository).findById(1L);
    }

    @Test
    void testListarBarbearias() {
        List<Barbearia> barbearias = List.of(getSampleBarbearia(1L), getSampleBarbearia(2L));

        when(barbeariaRepository.findAll()).thenReturn(barbearias);

        List<Barbearia> result = barbeariaService.listarBarbearias();

        assertEquals(2, result.size());
        verify(barbeariaRepository).findAll();
    }
}



