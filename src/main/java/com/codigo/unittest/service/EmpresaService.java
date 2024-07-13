package com.codigo.unittest.service;

import com.codigo.unittest.aggregates.request.EmpresaRequest;
import com.codigo.unittest.aggregates.response.BaseResponse;
import com.codigo.unittest.entity.Empresa;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface EmpresaService {

    ResponseEntity<BaseResponse<Empresa>> crear(EmpresaRequest request);
    ResponseEntity<BaseResponse<Empresa>> obtenerEmpresa(Long id);
    ResponseEntity<BaseResponse<List<Empresa>>> obtenerTodos();
    ResponseEntity<BaseResponse<Empresa>> actualizar(Long id, EmpresaRequest request);
    ResponseEntity<BaseResponse<Empresa>> delete(Long id);
    ResponseEntity<BaseResponse<Empresa>> obtenerEmpresaXNumDoc(String numDocu);

}
