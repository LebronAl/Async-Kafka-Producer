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

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;

public class Producer extends Thread {

  private final String topic;
  private final int numRecords;
  private final int id;
  private final CountDownLatch latch;
  private final DataGenerator generator;
  private final KafkaProducer<Integer, byte[]> kafkaProducer;

  public Producer(final int id, final int numRecords, final CountDownLatch latch,
      final String type) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, type);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 163840);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
    props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KafkaPartitioner.class.getName());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

    this.topic = KafkaProperties.TOPIC;
    this.numRecords = numRecords;
    this.latch = latch;
    this.id = id;
    this.generator = new DataGenerator(numRecords);
    kafkaProducer = new KafkaProducer<>(props);
  }


  @Override
  public void run() {
    AtomicInteger successNum = new AtomicInteger();
    int cnt = 0;
    while (generator.hasNext()) {
      byte[] message = generator.next().toArray();
      kafkaProducer.send(new ProducerRecord<>(topic,
          cnt++,
          message), (recordMetadata, e) -> {
        if (e != null) {
          System.out.println("Error occurred" + e.getMessage());
        } else {
          if (successNum.get() % 100000 == 0) {
            System.out
                .println(id + " : Producer sent " + successNum.get() + " records successfully");
          }
          if (successNum.incrementAndGet() == numRecords) {
            latch.countDown();
            System.out
                .println(id + " : Producer sent " + numRecords + " records successfully;Finished");
          }
        }
      });
    }
  }
}

