/*
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.cli.gatewayapi.declarative.command;

import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import io.apiman.cli.command.declarative.command.AbstractApplyCommand;
import io.apiman.cli.command.declarative.model.BaseDeclaration;
import io.apiman.cli.command.declarative.model.DeclarativeGateway;
import io.apiman.cli.command.gateway.model.GatewayConfig;
import io.apiman.cli.gatewayapi.GatewayApi;
import io.apiman.cli.gatewayapi.GatewayHelper;
import io.apiman.cli.gatewayapi.command.factory.GatewayApiFactory;
import io.apiman.cli.gatewayapi.model.GatewayApiDataModel;
import io.apiman.cli.util.LogUtil;
import io.apiman.cli.util.PolicyResolver;
import io.apiman.gateway.engine.beans.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Apply a gateway declaration.
 *
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@Parameters(commandDescription = "Apply Apiman Gateway declaration")
public class GatewayApplyCommand extends AbstractApplyCommand
        implements GatewayHelper {

    private static final Logger LOGGER = LogManager.getLogger(GatewayApplyCommand.class);
    private final GatewayApiFactory apiFactory;
    private final PolicyResolver policyResolver;

    @Inject
    public GatewayApplyCommand(GatewayApiFactory apiFactory,
                               PolicyResolver policyResolver) {
        super();
        this.apiFactory = apiFactory;
        this.policyResolver = policyResolver;
    }

    @Override
    protected void applyDeclaration(BaseDeclaration declaration) {
        GatewayApiDataModel dataModel = new GatewayApiDataModel(declaration, policyResolver);
        // Do gateway status checks: Tests whether gateways exist and advertise as up/available.
        doGatewayStatusChecks(dataModel);
        // Finally, publish.
        publishAll(dataModel);
    }

    private void doGatewayStatusChecks(GatewayApiDataModel dataModel) {
        dataModel.getGatewaysMap().values().forEach(this::isGatewayActive);
    }

    private boolean isGatewayActive(DeclarativeGateway gateway) {
        LOGGER.debug("Checking Gateway {} status", gateway.getName());
        GatewayApi client = buildGatewayApiClient(gateway.getConfig());
        return statusCheck(client, gateway.getConfig().getEndpoint());
    }

    private void publishAll(GatewayApiDataModel dataModel) {
        for (Map.Entry<Api, List<DeclarativeGateway>> entry : dataModel.getApiToGatewaysMap().entrySet()) {
            List<DeclarativeGateway> targetGateways = entry.getValue();
            Api targetApi = entry.getKey();
            targetGateways.forEach(gateway -> publishApi(targetApi, gateway));
        }
    }

    private void publishApi(Api api, DeclarativeGateway gateway) {
        GatewayConfig config = gateway.getConfig();
        // Remember, we're publishing to the gateways listed in the declaration, NOT from config.
        GatewayApi client = buildGatewayApiClient(config);
        LOGGER.info("Publishing {} to {}", api, gateway.getConfig().getEndpoint());
        // I don't like the way this conflates stateful and non-stateful
        callAndCatch(() -> client.publishApi(api));
    }

    private GatewayApi buildGatewayApiClient(GatewayConfig config) {
        return apiFactory.build(config.getEndpoint(),
                config.getUsername(),
                config.getPassword(),
                LogUtil.isLogDebug());
    }
}
