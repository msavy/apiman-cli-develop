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
package io.apiman.cli.gatewayapi.command.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import io.apiman.cli.exception.CommandException;
import io.apiman.cli.gatewayapi.GatewayApi;
import io.apiman.cli.gatewayapi.GatewayHelper;
import io.apiman.cli.gatewayapi.command.common.AbstractGatewayCommand;
import io.apiman.cli.gatewayapi.command.factory.GatewayApiService;

/**
 * Unregister client entity.
 *
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@Parameters(commandDescription = "Unregister a Client App")
public class UnregisterClientCommand extends AbstractGatewayCommand implements GatewayHelper {

    @Parameter(names = "--org", description = "Organization ID", required = true)
    private String orgId;

    @Parameter(names = "--client", description = "Client App ID", required = true)
    private String clientId;

    @Parameter(names = "--version", description = "API Version", required = true)
    private String version;

    @Inject
    protected UnregisterClientCommand(GatewayApiService apiFactory) {
        super(apiFactory);
    }

    @Override
    public void performFinalAction(JCommander parser) throws CommandException {
        GatewayApi gatewayApi = getGatewayApiService().buildGatewayApiClient();
        // Do status check
        statusCheck(gatewayApi, getGatewayConfig().getGatewayApiEndpoint());
        // Attempt retire
        callAndCatch(() -> gatewayApi.unregisterClient(orgId, clientId, version));
    }

}
