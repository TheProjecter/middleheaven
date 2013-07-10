package org.middleheaven.mail.service;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.middleheaven.mail.MailAsynchrounsCallback;
import org.middleheaven.mail.MailAsynchrounsCallbackDecorator;
import org.middleheaven.mail.MailException;
import org.middleheaven.mail.MailMessage;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.mail.MailSendingServiceDecorator;
import org.middleheaven.mail.MailTransmissionResult;

/**
 * Creates an asynchronous decorator dor a {@link MailSendingService};
 */
public class QueueMailSendingService extends MailSendingServiceDecorator {

	private BlockingQueue<QueuedMessage> queue = new PriorityBlockingQueue<QueuedMessage>(100,new Comparator<QueuedMessage>(){

		@Override
		public int compare(QueuedMessage m1, QueuedMessage m2) {
			return m1.message.getPriority().compareTo(m2.message.getPriority());
		}

	});

	private MailSenderThread mailSenderThread;

	/**
	 * 
	 * Constructor.
	 * @param original
	 */
	public QueueMailSendingService(MailSendingService original) {
		super(original);

		this.mailSenderThread = new MailSenderThread(queue, original);
	
	}

	/**
	 * 
	 * Constructor.
	 * @param original
	 * @param queue
	 */
	public QueueMailSendingService(MailSendingService original, BlockingQueue<QueuedMessage> queue) {
		this(original);
		setQueue(queue);
	}

	public final void setQueue(BlockingQueue<QueuedMessage> queue){
		BlockingQueue<QueuedMessage> oldQueue = this.queue; 
		this.queue = queue;
		oldQueue.drainTo(queue);
	}

	@Override
	public void send(MailMessage message) throws MailException {
		send(message, null);
	}

	@Override
	public void send(MailMessage message, MailAsynchrounsCallback callback)throws MailException {
		
		if (!this.mailSenderThread.isAlive()){
			mailSenderThread.start();
		}
		
		queue.offer(new QueuedMessage(message,callback));
	}


	private static class MailSenderThread extends Thread {

		private BlockingQueue<QueuedMessage> queue;
		private MailSendingService original;

		public MailSenderThread(BlockingQueue<QueuedMessage> queue, MailSendingService original) {
			super();
			this.queue = queue;
			this.original = original;
		}

		public void run(){
			while (true){
				try{
					QueuedMessage qm = queue.take();
					try{
						original.send(qm.getMessage(), new MailAsynchrounsCallbackDecorator(qm.getCallback()));

					} catch (Exception e){
						qm.getCallback().onSent(new MailTransmissionResult(qm.getMessage(), e));
					}
				} catch (InterruptedException e){
					break;
				}
			}
		}
	}

	private static class QueuedMessage{
		MailMessage message;
		MailAsynchrounsCallback callback;

		public QueuedMessage(MailMessage message,
				MailAsynchrounsCallback callback) {
			this.message = message;
			this.callback = callback;
		}

		public MailMessage getMessage() {
			return message;
		}

		public MailAsynchrounsCallback getCallback() {
			return callback;
		}

	}

}
