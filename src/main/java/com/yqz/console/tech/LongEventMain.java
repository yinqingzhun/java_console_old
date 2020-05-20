package com.yqz.console.tech;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain
{
    public static void main(String[] args) throws Exception
    {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        // Connect the handler
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event: " + event.value));

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++)
        {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }

    public static class LongEvent
    {
        private long value;

        public void set(long value)
        {
            this.value = value;
        }
    }

    public static class LongEventFactory implements EventFactory<LongEvent>
    {
        public LongEvent newInstance()
        {
            return new LongEvent();
        }
    }

    public static class LongEventHandler implements EventHandler<LongEvent>
    {
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch)
        {
            System.out.println("Event: " + event);
        }
    }

    public static class LongEventProducerWithTranslator
    {
        private final RingBuffer<LongEvent> ringBuffer;

        public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer)
        {
            this.ringBuffer = ringBuffer;
        }

        private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
                new EventTranslatorOneArg<LongEvent, ByteBuffer>()
                {
                    public void translateTo(LongEvent event, long sequence, ByteBuffer bb)
                    {
                        event.set(bb.getLong(0));
                    }
                };

        public void onData(ByteBuffer bb)
        {
            ringBuffer.publishEvent(TRANSLATOR, bb);
        }
    }
}