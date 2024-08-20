package com.dc.monitoringtool.architecture.rest;

import com.dc.monitoringtool.architecture.rest.dto.MonitoringResult;
import com.dc.monitoringtool.domain.MonitoringResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/monitoring-results")
public class MonitoringResultController {

//    private final MonitoringResultService monitoringResultService;
//
//    @GetMapping
//    public ResponseEntity<List<MonitoringResult>> getAllResults() {
//        List<MonitoringResult> results = monitoringResultService.getAllResults();
//        return ResponseEntity.ok(results);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MonitoringResult> getResultById(@PathVariable String id) {
//        Optional<MonitoringResult> result = monitoringResultService.getResultById(id);
//        return result.map(ResponseEntity::ok)
//                     .orElseGet(() -> ResponseEntity.notFound().build());
//    }
}