package com.MakeupStore.MakeupFrontend.Controller;


import com.MakeupStore.MakeupFrontend.dto.MakeupProductDTO;
import com.MakeupStore.MakeupFrontend.models.MakeupProduct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class WebController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_API = "http://localhost:8080/api/v1/makeupproducts";

    @GetMapping("/productos")
    public String mostrarProductos(@RequestParam(defaultValue = "0") int page,
                                   Model model) {
        String urlConPaginacion = URL_API + "?page=" + page + "&size=5";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                urlConPaginacion,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> productosMap = (List<Map<String, Object>>) response.getBody().get("Products");
            int totalPages = (int) response.getBody().get("TotalPages");
            int currentPage = (int) response.getBody().get("CurrentPage");

            List<MakeupProduct> productos = new ArrayList<>();
            for (Map<String, Object> map : productosMap) {
                MakeupProduct producto = new MakeupProduct();
                producto.setId(UUID.fromString((String) map.get("id")));
                producto.setProductName((String) map.get("ProductName"));
                producto.setCategory((String) map.get("ProductCategory"));
                Object cantidad = map.get("ProductQuantity");
                producto.setMakeupProductQuantity(cantidad != null ? (Integer) cantidad : 0);
                productos.add(producto);
            }

            model.addAttribute("productos", productos);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", currentPage);
        }

        return "lista-productos";
    }


    @GetMapping("/productos/crear")
    public String mostrarFormularioCrearProducto(Model model) {
        model.addAttribute("producto", new MakeupProductDTO());
        return "crear-producto";
    }

    @PostMapping("/productos/crear")
    public String crearProducto(@ModelAttribute("producto") MakeupProductDTO dto) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ProductName", dto.getProductName());
        requestBody.put("ProductCategory", dto.getProductCategory());
        requestBody.put("ProductQuantity", dto.getProductQuantity());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        restTemplate.postForEntity(URL_API, requestEntity, String.class);

        return "redirect:/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditarProducto(@PathVariable UUID id, Model model) {
        String url = URL_API + "/" + id;
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> productMap = (Map<String, Object>) response.getBody().get("Product");

            MakeupProductDTO dto = new MakeupProductDTO();
            dto.setId(UUID.fromString((String) productMap.get("id")));
            dto.setProductName((String) productMap.get("ProductName"));
            dto.setProductCategory((String) productMap.get("ProductCategory"));
            Object cantidad = productMap.get("ProductQuantity");
            dto.setProductQuantity(cantidad != null ? (Integer) cantidad : 0);

            model.addAttribute("producto", dto);
            return "editar-producto";
        }

        return "redirect:/productos";
    }

    @PostMapping("/productos/editar")
    public String editarProducto(@ModelAttribute("producto") MakeupProductDTO dto) {
        String urlConId = URL_API + "/" + dto.getId();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ProductName", dto.getProductName());
        requestBody.put("ProductCategory", dto.getProductCategory());
        requestBody.put("ProductQuantity", dto.getProductQuantity());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(urlConId, HttpMethod.PUT, requestEntity, String.class);

        return "redirect:/productos";
    }


    @GetMapping("/productos/eliminar/{id}")
    public String mostrarConfirmacionEliminar(@PathVariable UUID id, Model model) {
        String url = URL_API + "/" + id;
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> productMap = (Map<String, Object>) response.getBody().get("Product");

            MakeupProductDTO dto = new MakeupProductDTO();
            dto.setId(UUID.fromString((String) productMap.get("id")));
            dto.setProductName((String) productMap.get("ProductName"));
            dto.setProductCategory((String) productMap.get("ProductCategory"));
            Object cantidad = productMap.get("ProductQuantity");
            dto.setProductQuantity(cantidad != null ? (Integer) cantidad : 0);

            model.addAttribute("producto", dto);
            return "confirmar-eliminar";
        }

        return "redirect:/productos";
    }

    @PostMapping("/productos/eliminar")
    public String eliminarProducto(@ModelAttribute("producto") MakeupProductDTO dto) {
        restTemplate.exchange(
                URL_API + "/" + dto.getId(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        return "redirect:/productos";
    }




}
