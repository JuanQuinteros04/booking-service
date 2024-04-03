package com.booking.client;

import com.commons.client.model.DTO.ClientDTO;
import com.commons.client.model.DTO.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.List;

@FeignClient(name = "client-service", url = "localhost:2001")
public interface ClientFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/clients")
    List<ClientResponse> findAllClients();

    @RequestMapping(method = RequestMethod.GET, value = "/clients/{id}")
    ClientResponse findClientById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/clients")
    ClientResponse createClient(ClientDTO clientDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/clients/{id}")
    void updateClient(@PathVariable("id") Long id, ClientDTO clientDTO);

    @RequestMapping(method = RequestMethod.DELETE, value = "/clients")
    void deleteClient(Long id);

}
