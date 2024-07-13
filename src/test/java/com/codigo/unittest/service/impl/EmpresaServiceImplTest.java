package com.codigo.unittest.service.impl;

import com.codigo.unittest.aggregates.constants.Constants;
import com.codigo.unittest.aggregates.request.EmpresaRequest;
import com.codigo.unittest.aggregates.response.BaseResponse;
import com.codigo.unittest.dao.EmpresaRepository;
import com.codigo.unittest.entity.Empresa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EmpresaServiceImplTest {

    //WIREMOCK
    @Mock
    private EmpresaRepository empresaRepository;
    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private Empresa empresa;
    private EmpresaRequest empresaRequest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        empresa = new Empresa();
        empresa.setId(1L);
        empresaRequest = new EmpresaRequest();
        empresaRequest.setNumeroDocumento("123456789");
    }

    @Test
    public void testCrearEmpresaExistente() {
        // Preparación de datos de prueba
        //EmpresaRequest request = new EmpresaRequest();
        //request.setNumeroDocumento("123456789");

        // Configuración del comportamiento del mock
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(true);

        // Llamada al método bajo prueba
        ResponseEntity<BaseResponse<Empresa>> response = empresaService.crear(empresaRequest);

        // Verificaciones o aserciones
        assertEquals(Constants.CODE_EXIST, response.getBody().getCode());
        assertEquals(Constants.MSJ_EXIST, response.getBody().getMessage());
        assertTrue(response.getBody().getObjeto().isEmpty());

    }
    @Test
    public void testCrearEmpresaNueva() {
        // Datos de prueba
        //EmpresaRequest request = new EmpresaRequest();
        //request.setNumeroDocumento("987654321");
        //Empresa empresa = new Empresa();

        // Configuración del mock
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // Llamada al método bajo prueba
        ResponseEntity<BaseResponse<Empresa>> response = empresaService.crear(empresaRequest);

        // Verificaciones o aserciones
        assertEquals(Constants.CODE_OK, response.getBody().getCode());
        assertEquals(Constants.MSJ_OK, response.getBody().getMessage());
        assertTrue(response.getBody().getObjeto().isPresent());
        assertSame(empresa, response.getBody().getObjeto().get());

    }
    @Test
    void testObtenerEmpresaExistente(){
        //ARRANGE
        Long id = empresa.getId();
        //Empresa empresa = new Empresa();

        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));

        //ACT
        ResponseEntity<BaseResponse<Empresa>> response = empresaService.obtenerEmpresa(id);

        //assert
        assertTrue(response.getBody().getObjeto().isPresent());
        assertEquals(Constants.CODE_OK, response.getBody().getCode());
        assertEquals(Constants.MSJ_OK, response.getBody().getMessage());
        assertSame(empresa, response.getBody().getObjeto().get());
    }

    @Test
    void testObtenerEmpresaNoExistente(){
        Long id = 1L;
        when(empresaRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse<Empresa>> response = empresaService.obtenerEmpresa(id);

        assertFalse(response.getBody().getObjeto().isPresent());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, response.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST, response.getBody().getMessage());
    }

    @Test
    void testObtenerTodosExito(){
        //ARRANGE
        Empresa empresa1 = new Empresa();
        Empresa empresa2 = new Empresa();
        List<Empresa> lstEmpresa = Arrays.asList(empresa1,empresa2);

        when(empresaRepository.findAll()).thenReturn(lstEmpresa);
        //act
        ResponseEntity<BaseResponse<List<Empresa>>> response = empresaService.obtenerTodos();

        //assert
        assertFalse(response.getBody().getObjeto().isEmpty());
        //assertTrue(response.getBody().getObjeto().isPresent());
        assertEquals(Constants.CODE_OK, response.getBody().getCode());
        assertEquals(Constants.MSJ_OK, response.getBody().getMessage());

    }
    @Test
    void testObtenerTodosSinExito(){
        //ARRANGE

        when(empresaRepository.findAll()).thenReturn(Collections.emptyList());
        //act
        ResponseEntity<BaseResponse<List<Empresa>>> response = empresaService.obtenerTodos();

        //assert
        assertTrue(response.getBody().getObjeto().isEmpty());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, response.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST, response.getBody().getMessage());

    }
    @Test
    public void testActualizarEmpresaExistente() {
        Long id = 1L;
        EmpresaRequest request = new EmpresaRequest();  // Configura según tus atributos
        Empresa empresaRecuperada = new Empresa();  // Configura según tus atributos
        Empresa empresaActualizada = new Empresa();  // Configura según tus atributos

        when(empresaRepository.existsById(id)).thenReturn(true);
        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresaRecuperada));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaActualizada);

        ResponseEntity<BaseResponse<Empresa>> response = empresaService.actualizar(id, request);

        assertEquals(Constants.CODE_OK, response.getBody().getCode());
        assertEquals(Constants.MSJ_OK, response.getBody().getMessage());
        assertTrue(response.getBody().getObjeto().isPresent());
        assertSame(empresaActualizada, response.getBody().getObjeto().get());

    }

    @Test
    public void testActualizarEmpresaNoExistente() {
        Long id = 1L;
        EmpresaRequest request = new EmpresaRequest();  // Configura según tus atributos

        when(empresaRepository.existsById(id)).thenReturn(false);

        ResponseEntity<BaseResponse<Empresa>> response = empresaService.actualizar(id, request);

        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, response.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST, response.getBody().getMessage());
        assertTrue(response.getBody().getObjeto().isEmpty());

    }

}