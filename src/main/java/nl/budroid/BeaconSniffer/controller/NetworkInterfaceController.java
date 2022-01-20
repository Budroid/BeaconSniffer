package nl.budroid.BeaconSniffer.controller;

import nl.budroid.BeaconSniffer.domain.AvailableNIF;
import nl.budroid.BeaconSniffer.service.NetworkInterfaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class NetworkInterfaceController {

    final NetworkInterfaceService networkInterfaceService;

    public NetworkInterfaceController(NetworkInterfaceService networkInterfaceService) {
        this.networkInterfaceService = networkInterfaceService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<AvailableNIF>> getAllInterfaces() {
        List<AvailableNIF> allIterfaces;
        try {
            allIterfaces = networkInterfaceService.getAvailableNetworkInterfaces();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Fatsoenlijke error teruggeven
            return ResponseEntity.internalServerError()
                    .build();
        }
        return ResponseEntity.ok(allIterfaces);
    }

}
