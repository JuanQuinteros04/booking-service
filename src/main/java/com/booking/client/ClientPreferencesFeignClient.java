package com.booking.client;

import com.commons.preferences.model.DTO.ClientPreferencesDTO;
import com.commons.preferences.model.DTO.ClientPreferencesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "clientpreferences-service", url = "localhost:2004")
public interface ClientPreferencesFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/client/preferences")
    List<ClientPreferencesResponse> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/client/preferences/{id}")
    ClientPreferencesResponse findById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/client/preferences/client/{id}")
    ClientPreferencesResponse findByClientId(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/client/preferences")
    ClientPreferencesResponse createClientPreferences(ClientPreferencesDTO userPreferencesDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/client/preferences/{id}")
    void updateClientPreferences(@PathVariable("id") Long id, ClientPreferencesDTO userPreferencesDTO);

    @RequestMapping(method = RequestMethod.DELETE, value = "/client/preferences/{id}")
    void deleteClientPreferences(@PathVariable("id") Long id);

}
