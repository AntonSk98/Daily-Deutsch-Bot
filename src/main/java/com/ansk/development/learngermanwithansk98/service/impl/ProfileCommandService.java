package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.api.ICommandService;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.Profile;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.PROFILE;

/**
 * For testing purposes implementation of {@link ICommandService} with support of {@link AbstractCommandService}.
 *
 * @author Anton Skripin
 */
@Service
public class ProfileCommandService extends AbstractCommandService implements ICommandService {

    private final OutputGateway outputGateway;

    protected ProfileCommandService(CommandsConfiguration commandsConfiguration,
                                    OutputGateway outputGateway,
                                    CommandCache commandCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
    }

    @Override
    public Command supportedCommand() {
        return PROFILE;
    }

    @Override
    public void finishExecute(CommandParameters commandParameters) {
        outputGateway.sendPlainMessage(commandParameters.chatId(), "Command finished!");
    }

    @Override
    public AbstractCommandModel<Profile> supportedCommandModel() {
        return Profile
                .init()
                .addMapping("name", Profile::setName)
                .addMapping("surname", Profile::setSurname)
                .addMapping("age", (model, age) -> model.setAge(Integer.parseInt(age)));
    }
}
