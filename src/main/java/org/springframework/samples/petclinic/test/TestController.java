package org.springframework.samples.petclinic.test;

import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class TestController {

    private final VetRepository vets;
    private final JdbcTemplate jdbcTemplate;

    public TestController(VetRepository vetRepository, JdbcTemplate jdbcTemplate) {
        this.vets = vetRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/test/vets")
    public Collection<Vet> vets() {
        return this.vets.findAll();
    }

    @PostMapping("/test/generate-vets")
    public String generateVets() {
        String sql = "INSERT INTO vets (first_name, last_name) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            batchArgs.add(new Object[]{"FirstName" + i, "LastName" + i});
            if (batchArgs.size() == 1000) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear();
            }
        }
        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
        return "Generated 100,000 vets.";
    }
}
