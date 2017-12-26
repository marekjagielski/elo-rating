package com.elorating.service.email;

import com.elorating.model.Match;
import com.elorating.utils.DateUtils;
import org.thymeleaf.context.Context;

public class ScheduledMatchEmail extends EmailBuilder {

    private Match scheduledMatch;

    public ScheduledMatchEmail(Match scheduledMatch, String originUrl) {
        this.scheduledMatch = scheduledMatch;
        this.originUrl = originUrl;
    }

    @Override
    public void buildRecipient() {
        email.setRecipient(scheduledMatch.getPlayerTwo().getUser().getEmail());
    }

    @Override
    public void buildSubject() {
        email.setSubject(EmailStrings.SCHEDULED_MATCH);
    }

    @Override
    public void buildTemplateName() {
        email.setTemplateName(EmailStrings.SCHEDULED_MATCH_TEMPLATE);
    }

    @Override
    public void buildContext() {
        Context context = email.getContext();
        String redirectUrl = originUrl + "/leagues/" + this.scheduledMatch.getLeague().getId();
        context.setVariable("redirectUrl", redirectUrl);
        context.setVariable("username", this.scheduledMatch.getPlayerOne().getUsername());
        context.setVariable("matchtime", DateUtils.getDateTime(this.scheduledMatch.getDate()));
    }
}
