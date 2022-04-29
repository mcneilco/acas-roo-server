package com.labsynch.labseer.utils;

import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ExperimentValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class SimpleJsonDateTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJsonDateTest.class);

    @Test
    public void parseContainerValueDate() throws Exception {
        String json = "{\"dateValue\":-68400000}";
        ContainerValue value = ContainerValue.fromJsonToContainerValue(json);
        logger.info("here is the date value: " + value.getDateValue());
    }

    // @Test
    public void parseContainerValueIsoDate() throws Exception {
        String json = "{\"dateValue\":\"2007-04-05T14:30Z\"}";
        ContainerValue value = ContainerValue.fromJsonToContainerValue(json);
        logger.info("here is the date value: " + value.getDateValue());
    }

    @Test
    public void parseExperimentValueDate() throws Exception {
        String json = "{\"dateValue\":-68400000, \"numericValue\":123.456789}";
        ExperimentValue value = ExperimentValue.fromJsonToExperimentValue(json);
        logger.info("here is the date value: " + value.getDateValue());
        logger.info("here is the numeric value: " + value.getNumericValue());

    }
}
