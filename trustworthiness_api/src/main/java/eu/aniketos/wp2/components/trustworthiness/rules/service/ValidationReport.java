/**
 * 
 */
package eu.aniketos.wp2.components.trustworthiness.rules.service;

import java.util.Set;

/**
 * represents the result of the validation process
 * 
 * @author Hisain Elshaafi (TSSG)
 *
 */
public interface ValidationReport {
  /**
   * @return all messages in this report
   */
  Set<Message> getMessages();

  /**
   * @return all messages of specified type in this report
   */
  Set<Message> getMessagesByType(Message.Type type);

  /**
   * @return true if this report contains message with 
   *  specified key, false otherwise
   */
  boolean contains(String messageKey);
  
  /**
   * adds specified message to this report
   */
  boolean addMessage(Message message);
}
// @extract-end
