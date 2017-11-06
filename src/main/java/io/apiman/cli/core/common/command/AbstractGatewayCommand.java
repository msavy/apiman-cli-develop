/*
 * Copyright 2016 Pete Cornish
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

package io.apiman.cli.core.common.command;

import com.beust.jcommander.ParametersDelegate;
import io.apiman.cli.command.AbstractFinalCommand;
import io.apiman.cli.command.GatewayCommon;

/**
 * Common model CRUD functionality.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public abstract class AbstractGatewayCommand extends AbstractFinalCommand {
    @ParametersDelegate
    private GatewayCommon gatewayCommonConfig = new GatewayCommon();

    public GatewayCommon getGatewayConfig() {
        return gatewayCommonConfig;
    }
}
