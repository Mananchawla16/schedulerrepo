package com.intuit.gatling.common.helper

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._
import scala.collection._
import scala.language.postfixOps

class TicketCache {
  val ticketCache  : concurrent.Map[String,TicketInfo] = new ConcurrentHashMap[String, TicketInfo] asScala
  val defaultOpaqueTicketCacheExpiryMs: String = "86100000"
  val defaultWorkforceTicketCacheExpiryMs: String = "3300000"
  val ticketCacheExpirationMs: Long = System.getProperty("ticketCacheExpirationMs", defaultOpaqueTicketCacheExpiryMs).toLong
  val workforceTicketCacheExpirationMs: Long = System.getProperty("workforceTicketCacheExpirationMs", defaultWorkforceTicketCacheExpiryMs).toLong

  /**
   * Check if ticket info is cached and hasn't expired
   * @param key username
   * @return true if ticket exists for given username
   */
  def isValidTicketAvailable(key: String, isWorkforceUser: Boolean) : Boolean = {
    ticketCache.get(key)
      .exists(ticket => !isTicketExpired(key, ticket.ticketCreatedTime, isWorkforceUser))
  }

  /**
   * Cache ticket info
   * @param key username
   * @param value object of type TicketInfo
   * @return TicketInfo
   */
  def cacheTicket(key: String, value: TicketInfo): Option[Object] = {
    ticketCache.putIfAbsent(key, value)
  }

  /**
   * Remove ticket info from cache
   * @param key username
   * @return
   */
  def removeTicket(key: String) = {
    ticketCache.remove(key)
  }

  /**
   * Get ticket info from cache
   * @param key username
   * @return an object of type TicketInfo
   */
  def getTicket(key: String): TicketInfo = {
    var result : Option[TicketInfo] = None
    // WARNING: if the thread that is supposed to put a value in cache doesn't do it - this thread will block forever. This is mitigated by adding a pause between 0.5 to 1 second
    do {
      result = ticketCache.get(key)
      if (result == None) {
        Thread.sleep(100)
      }
    } while (result == None)

    result.get // returns the ticket info object
  }

  /**
   * Checks if the tickets expiry time if crosses the cache expiry time
   * if yes, removes ticket from caches
   */
  def isTicketExpired(username: String, ticketCreatedTime: Long, isWorkforceUser: Boolean) : Boolean = {
    val now: Long = System.currentTimeMillis()
    var isExpired = false
    if (isWorkforceUser) {
      isExpired = (now - ticketCreatedTime) > workforceTicketCacheExpirationMs
    } else {
      isExpired = (now - ticketCreatedTime) > ticketCacheExpirationMs
    }
    if (isExpired) {
      removeTicket(username)
    }
    isExpired
  }

}
/**
 * CLASS >> To create user object which has info about - userTicket, userAuthId,agentId - required for creating cache with userName
 */
case class TicketInfo(ticketId: String, authId: String, agentId:String, ticketCreatedTime:Long) {
}