package com.ciscolive.uccx.demo;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/*
 * This is a sample JMS message producer class that is used by the sample script webcallback.aef to publish a message to
 * the specified topic. 
 * 
 * This is not a production ready code and can not be used AS-IS.
 * 
 * @author jayas@cisco.com
 * copyright 2016 Cisco Systems Inc.
 */
public class MsgProducer {
	
	 ActiveMQConnectionFactory connectionFactory =null;
	 Connection connection = null;
	 
	
	
	
	public  synchronized static MsgProducer getInstance() {
		return new MsgProducer();
	}
	
	public void init(String brokerURL) throws Exception {
		// Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
	}
	
	public void stop() throws Exception
	{
		connection.close();
	}
	
	
	public void send(String topic, String msg) {
		Session session = null;
        try {
           
           

            // Create a Session
             session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(topic);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            TextMessage message = session.createTextMessage(msg);

            // Tell the producer to send the message
            producer.send(message);

          
            connection.close();
        }
        catch (Exception e) {
           //TODO:  Handle exception here. 
        	// throw a custom exception with error mesage and the cause as the exception caught here.
        	// This results in script throwing WFException
            e.printStackTrace();
        }
        finally {
        	if(session != null) {
        		  // Clean up
                try {
					session.close();
				} catch (Exception e) {
					//  Ignore the error.
				}
        	}	
        }
    }

}
