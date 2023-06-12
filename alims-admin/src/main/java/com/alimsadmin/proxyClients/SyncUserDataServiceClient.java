package com.alimsadmin.proxyClients;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.UserAccountDTO;
import com.alimsadmin.dto.UserSyncSendProxyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "core-alims-service", url = "http://localhost:8762/airline/dataSync")
public interface SyncUserDataServiceClient {

    @PostMapping("/syncUsersAndRoles")
    String postAdminUsersAndRole(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                 @RequestBody UserSyncSendProxyDTO dto);

    @GetMapping("/getAllUsersByProfileFromAirlineSystem/{airCode}")
    List<UserAccountDTO> getAllUsersByProfileFromAirlineSystem(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                               @PathVariable String airCode);
}

