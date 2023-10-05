package com.javafest.ServiceLagbe.event;

import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OTPRegistrationCompleteEvent extends ApplicationEvent {
    private WorkerUser user;

    public OTPRegistrationCompleteEvent(WorkerUser user) {
        super(user);
        this.user = user;
    }
}
