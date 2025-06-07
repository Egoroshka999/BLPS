package com.lab.blps.components;

import com.lab.blps.models.applications.User;
import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate for retrieving the current authenticated user.
 */
@Component("GetCurrentUserDelegate")
@Transactional
public class GetCurrentUserDelegate implements JavaDelegate {

    private final UserService userService;

    public GetCurrentUserDelegate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        User currentUser = userService.getCurrentUser();
        execution.setVariable("user", currentUser);
    }
}
