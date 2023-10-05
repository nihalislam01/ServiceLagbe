package com.javafest.ServiceLagbe.event;

import com.javafest.ServiceLagbe.users.general.GeneralUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordViaEmailEvent extends ApplicationEvent {

    private GeneralUser user;
    private String applicationUrl;

    public ResetPasswordViaEmailEvent(GeneralUser user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
