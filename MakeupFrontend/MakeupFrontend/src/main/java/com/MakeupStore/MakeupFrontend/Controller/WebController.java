package com.MakeupStore.MakeupFrontend.Controller;


import com.MakeupStore.MakeupFrontend.models.MakeupProduct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class WebController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_API = "http://localhost:8080/api/v1/makeupproducts";

    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                URL_API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            // Obtenemos la lista de productos como LinkedHashMap
            List<Map<String, Object>> productosMap = (List<Map<String, Object>>) response.getBody().get("Products");

            List<MakeupProduct> productos = new ArrayList<>();

            for (Map<String, Object> map : productosMap) {
                MakeupProduct producto = new MakeupProduct();
                producto.setId(UUID.fromString((String) map.get("id")));
                producto.setProductName((String) map.get("ProductName"));
                producto.setCategory((String) map.get("ProductCategory"));
                Object cantidad = map.get("MakeupProductQuantity");
                if (cantidad != null) {
                    producto.setMakeupProductQuantity((Integer) cantidad);
                } else {
                    producto.setMakeupProductQuantity(0); // o algún valor por defecto
                }

                productos.add(producto);
            }


            model.addAttribute("productos", productos);
        } else {
            model.addAttribute("productos", new ArrayList<>());
        }

        return "lista-productos";
    }
}
