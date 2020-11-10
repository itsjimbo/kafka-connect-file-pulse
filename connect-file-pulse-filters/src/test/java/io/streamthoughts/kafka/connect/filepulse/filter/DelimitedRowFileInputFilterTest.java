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
package io.streamthoughts.kafka.connect.filepulse.filter;

import io.streamthoughts.kafka.connect.filepulse.data.Type;
import io.streamthoughts.kafka.connect.filepulse.data.TypedStruct;
import io.streamthoughts.kafka.connect.filepulse.reader.RecordsIterable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.streamthoughts.kafka.connect.filepulse.config.DelimitedRowFilterConfig.READER_EXTRACT_COLUMN_NAME_CONFIG;
import static io.streamthoughts.kafka.connect.filepulse.config.DelimitedRowFilterConfig.READER_FIELD_COLUMNS_CONFIG;


public class DelimitedRowFileInputFilterTest {

    private Map<String, String> configs;

    private DelimitedRowFilter filter;


    private static final TypedStruct DEFAULT_STRUCT = TypedStruct.create()
        .put("message", "value1;2;true")
        .put("headers", Collections.singletonList("col1;col2;col3"));


    @Before
    public void setUp() {
        filter = new DelimitedRowFilter();
        configs = new HashMap<>();
    }

    @Test
    public void shouldAutoGeneratedSchemaGivenNoSchemaField() {
        filter.configure(configs);
        RecordsIterable<TypedStruct> output = filter.apply(null, DEFAULT_STRUCT, false);
        Assert.assertNotNull(output);
        Assert.assertEquals(1, output.size());

        final TypedStruct record = output.iterator().next();
        Assert.assertEquals("value1", record.getString("column1"));
        Assert.assertEquals("2", record.getString("column2"));
        Assert.assertEquals("true", record.getString("column3"));
    }

    @Test
    public void shouldExtractColumnNamesFromGivenField() {
        configs.put(READER_EXTRACT_COLUMN_NAME_CONFIG, "headers");
        filter.configure(configs);
        RecordsIterable<TypedStruct> output = filter.apply(null, DEFAULT_STRUCT, false);
        Assert.assertNotNull(output);
        Assert.assertEquals(1, output.size());

        final TypedStruct record = output.iterator().next();
        Assert.assertEquals("value1", record.getString("col1"));
        Assert.assertEquals("2", record.getString("col2"));
        Assert.assertEquals("true", record.getString("col3"));
    }

    @Test
    public void shouldUseConfiguredSchema() {
        configs.put(READER_FIELD_COLUMNS_CONFIG, "c1:STRING;c2:INTEGER;c3:BOOLEAN");
        filter.configure(configs);
        RecordsIterable<TypedStruct> output = filter.apply(null, DEFAULT_STRUCT, false);
        Assert.assertNotNull(output);
        Assert.assertEquals(1, output.size());

        final TypedStruct record = output.iterator().next();
        Assert.assertEquals(Type.STRING, record.get("c1").type());
        Assert.assertEquals(Type.INTEGER, record.get("c2").type());
        Assert.assertEquals(Type.BOOLEAN, record.get("c3").type());
        Assert.assertEquals("value1", record.getString("c1"));
        Assert.assertEquals(2, record.getInt("c2").intValue());
        Assert.assertTrue(record.getBoolean("c3"));
    }
}