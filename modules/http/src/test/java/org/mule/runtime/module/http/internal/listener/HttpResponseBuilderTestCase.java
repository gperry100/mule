/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.http.internal.listener;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.module.http.api.HttpHeaders.Names.CONTENT_LENGTH;
import org.mule.runtime.core.DefaultMuleMessage;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MutableMuleMessage;
import org.mule.runtime.module.http.internal.domain.response.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class HttpResponseBuilderTestCase
{

    public static final String EXAMPLE_STRING = "exampleString";
    private MuleContext muleContext;
    private MutableMuleMessage mockMuleMessage;
    private MuleEvent mockEvent;

    @Before
    public void setUp()
    {
        muleContext = mock(MuleContext.class, RETURNS_DEEP_STUBS);
        mockEvent = mock(MuleEvent.class);
        mockMuleMessage = mock(MutableMuleMessage.class);
    }

    @Test
    public void testContentLengthIsOverridden() throws Exception
    {
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        int contentLengthDifferingPayloadSize = 12;
        mockMuleMessage(new ByteArrayInputStream(EXAMPLE_STRING.getBytes(UTF_8)), contentLengthDifferingPayloadSize);

        HttpResponse httpResponse = httpResponseBuilder.build(new org.mule.runtime.module.http.internal.domain.response.HttpResponseBuilder(), mockEvent);
        assertThat(httpResponse.getHeaderValue(CONTENT_LENGTH), is(String.valueOf(EXAMPLE_STRING.length())));
    }

    private void mockMuleMessage(InputStream payload, int contentLength)
    {
        mockMuleMessage = new DefaultMuleMessage(payload, muleContext);
        mockMuleMessage.setOutboundProperty(CONTENT_LENGTH, contentLength);
        when(mockEvent.getMessage()).thenReturn(mockMuleMessage);
    }
}
