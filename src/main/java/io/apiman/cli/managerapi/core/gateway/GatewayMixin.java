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

package io.apiman.cli.managerapi.core.gateway;

import io.apiman.cli.core.common.command.ModelAction;
import io.apiman.cli.managerapi.core.gateway.model.Gateway;

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public interface GatewayMixin extends ModelAction<Gateway, GatewayApi> {
    @Override
    default Class<GatewayApi> getApiClass() {
        return GatewayApi.class;
    }

    @Override
    default Class<Gateway> getModelClass() {
        return Gateway.class;
    }
}