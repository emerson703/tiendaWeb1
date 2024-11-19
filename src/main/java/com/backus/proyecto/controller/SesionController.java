package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.repository.ClienteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/session")
public class SesionController {
    @Autowired
    private ClienteRepository clienteRepository;



    @GetMapping("/verificarSesion")
    public ResponseEntity<Map<String, Boolean>> verificarSesion(HttpSession session) {
        Boolean sesionActiva = session.getAttribute("email") != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("sesionActiva", sesionActiva);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/datosSesionAnonimo")
    public ResponseEntity<Map<String, Object>> obtenerDatosSesion(HttpSession session) {
        Map<String, Object> datosSesion = new HashMap<>();
        datosSesion.put("idClienteAnonimo", session.getAttribute("idClienteAnonimo"));
        datosSesion.put("email", session.getAttribute("email"));
        datosSesion.put("telefono", session.getAttribute("telefono"));
        datosSesion.put("nombre", session.getAttribute("nombre"));
        return ResponseEntity.ok(datosSesion);
    }

    @GetMapping("/datosSesion")
    public ResponseEntity<Map<String, Object>> obtenerDatosSesionCliente(HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        Map<String, Object> datosSesion = new HashMap<>();
        datosSesion.put("email", cliente.getEmail());
        datosSesion.put("telefono", cliente.getTelefono());
        datosSesion.put("nombre", cliente.getNombre());
        return ResponseEntity.ok(datosSesion);
    }

    @DeleteMapping("/cerrarSesion")
    public ResponseEntity<Void> cerrarSesion(HttpSession session) {
        //session.invalidate(); // Invalidar la sesión completa
        session.removeAttribute("cliente");
        return ResponseEntity.ok().build();
    }


}
