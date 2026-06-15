package com.labsynch.labseer.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExperimentRetentionServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ExperimentRetentionServiceImpl experimentRetentionService;

    public ExperimentRetentionServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHardDeleteExpiredExperiments() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(3);
        int deleted = experimentRetentionService.hardDeleteExpiredExperiments();
        assertEquals(3, deleted);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
    }
}
