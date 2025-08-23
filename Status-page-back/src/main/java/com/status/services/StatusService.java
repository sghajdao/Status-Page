package com.status.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.status.configurations.UnsafeRestTemplate;
import com.status.dto.SwitchStatus;
import com.status.entities.SwitchEntity;
import com.status.repositories.SwitchRepository;

@Service
public class StatusService {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SwitchRepository switchRepository;

    public StatusService() throws Exception {
        this.restTemplate = UnsafeRestTemplate.create(); // SSL disabled RestTemplate
    }

    @Scheduled(fixedRate = 10 * 1000) // every 10 seconds
    public void getStatus() {
        List<SwitchEntity> switches = switchRepository.findAll();
        List<SwitchStatus> response = new ArrayList<>();

        for (SwitchEntity s : switches) {
            boolean isUp = checkHostStatus(s.getIp());
            SwitchStatus item = new SwitchStatus(s, isUp ? 0 : 1);
            response.add(item);
        }
        template.convertAndSend("/message", response);
    }

    private boolean checkHostStatus(String host) {
        try {
            if (isIpAddress(host)) {
                return testIcmpPing(host);
            } else {
                // Not a pure IP address - could be URL, hostname with port, or hostname
                if (looksLikeUrl(host)) {
                    return testHttpEndpoint(normalizeUrl(host));
                } else {
                    // Treat as hostname - try ICMP ping
                    return testIcmpPing(host);
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking status for host: " + host + " - " + e.getMessage());
            return false;
        }
    }

    private boolean isIpAddress(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Remove port if present (e.g., "192.168.1.1:8080" -> "192.168.1.1")
        String ipPart = input.split(":")[0];

        // IPv4 pattern
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        return ipPart.matches(ipv4Pattern);
    }

    private boolean looksLikeUrl(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Already has protocol
        if (input.startsWith("http://") || input.startsWith("https://")) {
            return true;
        }

        // Contains port number (likely a web service)
        if (input.matches(".*:\\d+.*")) {
            return true;
        }

        // Contains path or query parameters
        if (input.contains("/") || input.contains("?") || input.contains("#")) {
            return true;
        }

        // Contains common web-related subdomains
        if (input.matches("^(www\\.|api\\.|admin\\.|web\\.).*")) {
            return true;
        }

        // Contains common TLDs that are likely web services
        if (input.matches(".*\\.(com|org|net|edu|gov|io|co|uk|de|fr|jp|cn)(/.*)?$")) {
            return true;
        }

        return false;
    }

    private String normalizeUrl(String input) {
        if (input.startsWith("http://") || input.startsWith("https://")) {
            return input;
        }

        // Add http:// prefix if it looks like a URL but doesn't have protocol
        return "http://" + input;
    }

    private boolean testHttpEndpoint(String url) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.HEAD, 
                null, 
                String.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            return false;
        }
    }

    private boolean testIcmpPing(String host) {
        try {
            // Remove port if present for ICMP ping
            String hostOnly = host.split(":")[0];
            InetAddress inet = InetAddress.getByName(hostOnly);
            return inet.isReachable(5000); // 5 second timeout
        } catch (IOException e) {
            return false;
        }
    }
}
