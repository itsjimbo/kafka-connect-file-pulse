/*
 * Copyright 2019-2020 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.streamthoughts.kafka.connect.filepulse.config;

import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class ExplodeFilterConfig extends CommonFilterConfig {

    private static final String FILTER_EXPLODE = "EXPLODE_FILTER";

    /**
     * Creates a new {@link ExplodeFilterConfig} instance.
     *
     * @param originals the filter configuration.
     */
    public ExplodeFilterConfig(final Map<?, ?> originals) {
        super(configDef(), originals);
    }

    public static ConfigDef configDef() {
        int filterGroupCounter = 0;
        return new ConfigDef(CommonFilterConfig.configDef())
                .define(getOverwriteConfigKey(FILTER_EXPLODE, filterGroupCounter++))
                .define(getSourceConfigKey(FILTER_EXPLODE, filterGroupCounter++));
    }
}
