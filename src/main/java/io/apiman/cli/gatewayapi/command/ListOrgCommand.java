/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.cli.gatewayapi.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import io.apiman.cli.core.api.GatewayApi;
import io.apiman.cli.core.common.command.AbstractGatewayCommand;
import io.apiman.cli.gatewayapi.GatewayHelper;
import io.apiman.cli.exception.CommandException;
import io.apiman.cli.managerapi.management.factory.GatewayApiFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@Parameters(commandDescription = "List all Organization IDs")
public class ListOrgCommand extends AbstractGatewayCommand implements GatewayHelper {

    private GatewayApiFactory apiFactory;
    private Logger LOGGER = LogManager.getLogger(ListOrgCommand.class);

    @Override
    public void performAction(JCommander parser) throws CommandException {
        GatewayApi gatewayApi = buildGatewayApiClient(apiFactory, getGatewayConfig());
        // Do status check
        statusCheck(gatewayApi, getGatewayConfig().getGatewayApiEndpoint());
        // Get endpoint (if any)
        List<String> orgs = callAndCatch(getGatewayConfig().getGatewayApiEndpoint(),
                () -> gatewayApi.listOrgs());
        LOGGER.debug("Orgs returned: {}", orgs.size());
        // Sort case insensitively
        orgs.sort(String::compareToIgnoreCase);
        orgs.forEach(System.out::println);
    }

    @Inject
    public void setGatewayApiFactory(GatewayApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    protected boolean permitNoArgs() {
        return true;
    }
}