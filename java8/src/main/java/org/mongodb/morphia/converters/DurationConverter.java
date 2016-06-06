/*
 * Copyright (c) 2008-2016 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb.morphia.converters;

import org.mongodb.morphia.mapping.MappedField;

import java.time.Duration;

/**
 * This converter will take a {@link Duration} and convert it to its String form.
 * @see Duration#toString()
 */
public class DurationConverter extends TypeConverter implements SimpleValueConverter {
    private final NumberPadder padder = new NumberPadder(3);

    /**
     * Creates the Converter.
     */
    public DurationConverter() {
        super(Duration.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof Duration) {
            return val;
        }

        if (val instanceof Number) {
            long[] values = padder.extract(((Number) val).longValue());
            return Duration.ofSeconds(values[0], values[1] * 1_000_000);
        }

        if (val instanceof String) {
            return Duration.parse((CharSequence) val);
        }

        throw new IllegalArgumentException("Can't convert to Duration from " + val);
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}