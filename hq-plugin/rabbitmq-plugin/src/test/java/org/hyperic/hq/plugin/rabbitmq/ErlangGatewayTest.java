/**
 * NOTE: This copyright does *not* cover user programs that use Hyperic
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 *
 *  Copyright (C) [2010], VMware, Inc.
 *  This file is part of Hyperic.
 *
 *  Hyperic is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */
package org.hyperic.hq.plugin.rabbitmq;
 

import org.hyperic.hq.plugin.rabbitmq.core.*; 
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.admin.RabbitBrokerAdmin;
import org.springframework.amqp.rabbit.connection.SingleConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.erlang.core.ErlangTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
import java.util.List;

import static org.junit.Assert.*;

/**
 * ErlangGatewayTest
 * @author Helena Edelson
 */
@ContextConfiguration("classpath:/etc/rabbit-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Ignore("Need to set up a rabbit server for QA")
public class ErlangGatewayTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ErlangGateway erlangGatway;

    @Autowired
    private SingleConnectionFactory singleConnectionFactory;

    @Autowired
    private RabbitBrokerAdmin rabbitBrokerAdmin;

    @Autowired
    private Queue requestQueue;

    @Autowired
    private Queue responseQueue;

    @Autowired
    private RabbitGateway rabbitGateway;

    private String virtualHost;

    @Before
    public void before() {
        assertNotNull("erlangGatway should not be null", erlangGatway);
        assertNotNull("rabbitTemplate should not be null", rabbitTemplate);
        this.virtualHost = rabbitTemplate.getConnectionFactory().getVirtualHost();
    }

    @Test
    @Ignore("always throws ErlangConversionException")
    public void testErlangTemplate() {
        ErlangTemplate erlangTemplate  = rabbitBrokerAdmin.getErlangTemplate();
        assertNotNull("erlangTemplate must not be null", erlangTemplate);
        Object object = erlangTemplate.executeAndConvertRpc("rabbit_networking", "connection_info_all", virtualHost);
    }
 

    @Test
    public void getVirtualHosts() throws Exception {
        List<String> virtualHosts = erlangGatway.getVirtualHosts();
        assertNotNull(virtualHosts);
        assertTrue(virtualHosts.size() >= 0);
    }

    @Test
    public void getExchanges() throws Exception {
        List<Exchange> exchanges = erlangGatway.getExchanges(virtualHost);
        assertNotNull(exchanges);
    }

    @Test
    public void getChannels() {
        List<AmqpChannel> channels = erlangGatway.getChannels();
        for (AmqpChannel c : channels) {
            System.out.println(c);
        }
        // if we're just starting up the node, it won't have channels.
        // will add assertions after a set up is added.
    }



}
