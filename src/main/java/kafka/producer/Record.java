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

import java.util.Arrays;

public class Record {

  private String ip;
  private Long time;
  private String path;
  private String state;
  private byte[] array = new byte[KafkaProperties.RECORD_LENGTH];

  public Record(String ip, Long time, String path, String state) {
    this.ip = ip;
    this.time = time;
    this.path = path;
    this.state = state;
    Arrays.fill(array, (byte) 0);
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public byte[] toArray() {
    String str = String.format("[ip:%s][time:%d][path:%s][state:%s]", ip, time, path, state);
    if (str.length() > KafkaProperties.RECORD_LENGTH) {
      str = str.substring(0, KafkaProperties.RECORD_LENGTH);
    }
    byte[] strBytes = str.getBytes();
    System.arraycopy(strBytes, 0, array, 0, strBytes.length);
    return array;
  }
}
