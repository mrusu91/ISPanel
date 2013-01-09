package com.vrx.ispanel;

import java.io.Serializable;
import java.util.ArrayList;

public class SysClients implements Serializable {

 private static final long serialVersionUID = 3730209298392638091L; // this need to be the same as in server part

 ArrayList<String> allClients_IP;
 ArrayList<String> allClients_MAC;
 ArrayList<String> allClients_NAME;
 ArrayList<String> onlineClients_IP;

 private boolean noOnlineClients;
 private boolean noClients;

 public SysClients() {
  allClients_IP = new ArrayList<String>();
  allClients_MAC = new ArrayList<String>();
  allClients_NAME = new ArrayList<String>();
  onlineClients_IP = new ArrayList<String>();
 }

 public ArrayList<String> getAllClients_IP() {
  return allClients_IP;
 }

 public ArrayList<String> getAllClients_MAC() {
  return allClients_MAC;
 }

 public ArrayList<String> getAllClients_NAME() {
  return allClients_NAME;
 }

 public ArrayList<String> getOnlineClients() {
  return onlineClients_IP;
 }
 public boolean hasNoClients() {
  return noClients;
 }

 public boolean hasNoOnlineClients() {
  return noOnlineClients;
 }
}
