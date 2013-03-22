package org.opencoin.core.messages

/**
 * This simple trait is extended by all ResponseX classes. The purpose is to specify the 
 * appropriate return type of the Messages object.
 */
trait Response {
    val `type`: String
    val message_reference: Int
    val status_code: Int
    val status_description: String
}