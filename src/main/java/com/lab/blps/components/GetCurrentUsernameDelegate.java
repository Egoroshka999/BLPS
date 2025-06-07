package com.lab.blps.components;

import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Delegate for retrieving the username of the current authenticated user.
 */
@Component("GetCurrentUsernameDelegate")
public class GetCurrentUsernameDelegate implements JavaDelegate {

    private final UserService userService;

    public GetCurrentUsernameDelegate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String username = userService.getCurrentUsername();
        execution.setVariable("username", username);
    }
}
