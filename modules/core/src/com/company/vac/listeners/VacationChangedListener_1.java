package com.company.vac.listeners;

import com.company.vac.entity.Vacation;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.SilentException;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.UUID;

@Component("vac_VacationChangedListener_1")
public class VacationChangedListener_1 {

    @Inject
    private DataManager dataManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(EntityChangedEvent<Vacation, UUID> event) {

        Id<Vacation, UUID> entityId = event.getEntityId();
        EntityChangedEvent.Type changeType = event.getType();

        Vacation vacation = dataManager.load(entityId).view("_local").one();

        if (changeType == EntityChangedEvent.Type.CREATED) {

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
            headers.add("Authorization", "Basic " + "cG9ydGFsOjFRMnczZTRyNXR6eGN2Ym5t");
            headers.add("Content-Type", "application/json");

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<Vacation> request = new HttpEntity<Vacation>(vacation, headers);

            Boolean result = restTemplate.postForObject("http://c2shweb04/test28/hs/portal/doVacation", request, Boolean.class);

            if (result != null && result.equals(false)) throw new SilentException();
        }

    }
}