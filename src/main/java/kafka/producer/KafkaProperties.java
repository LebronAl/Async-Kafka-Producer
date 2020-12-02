/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package kafka.producer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class KafkaProperties {

  public static final String KAFKA_SERVER_URL = "localhost";
  public static final int KAFKA_SERVER_PORT = 9092;
  public static final String TOPIC = "test";
  public static final int RECORD_LENGTH = 1024;
  public static final int PARTITION_NUM = 10;
  public static final String PATH = System.getProperty("user.dir");
  public static final String STATE = "alive";
  public static String IP;

  static {
    try {
      IP = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      IP = "127.0.0.1";
    }
  }
}