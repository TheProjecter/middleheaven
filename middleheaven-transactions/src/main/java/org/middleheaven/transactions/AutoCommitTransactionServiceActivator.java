package org.middleheaven.transactions;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.InterceptorProxyHandler;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.InterceptionContext;
import org.middleheaven.core.wiring.InterceptorChain;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringInterceptor;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Mapper;

public class AutoCommitTransactionServiceActivator extends ServiceActivator {

	protected AutoCommitTransactionService autoCommitTransactionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(TransactionService.class));
	}

	@Override
	public void activate(ServiceContext serviceContext) {

		WiringService wiringService = serviceContext.getService(WiringService.class);

		wiringService.addConnector(new WiringConnector(){

			@Override
			public void connect(ConnectableBinder binder) {
				binder.addInterceptor(transactionInterceptor);
			}

		});
		autoCommitTransactionService =   new AutoCommitTransactionService();

		serviceContext.register(TransactionService.class,autoCommitTransactionService);
	}

	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(TransactionService.class);
	}

	final TransactionInterceptor transactionInterceptor = new TransactionInterceptor();

	private class TransactionInterceptor implements WiringInterceptor {


		@Override
		public void intercept(InterceptionContext context,	InterceptorChain chain) {
			chain.doChain(context);

			Object object = context.getObject();

			object = proxyfy(object, context.getTarget());

			context.setObject(object);
		}

		@SuppressWarnings("unchecked")
		public Object proxyfy (final Object original, Class<?> type){

			Enumerable<Method> all = Introspector.of(original.getClass()).inspect()
					.methods()
					.notInheritFromObject()
					.annotatedWith(Transactional.class).retriveAll();

			if (all.isEmpty()){
				return original;
			} else {

				final Set<String> names = all.map(new Mapper<String,Method>(){

					@Override
					public String apply(Method obj) {
						return obj.getName();
					}

				}).into(new HashSet<String>());

				return Introspector.of(type).newProxyInstance(new InterceptorProxyHandler (original){

					@Override
					protected boolean willIntercept(Object proxy,
							Object[] args, MethodDelegator delegator)
									throws Throwable {
						return names.contains(delegator.getName());
					}

					@Override
					protected Object doInstead(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
						Transaction t = autoCommitTransactionService.getTransaction();
						try {
							t.begin();
							Object o =  this.doOriginal(args, delegator);
							t.commit();
							return o;
						}catch (Throwable e){
							t.roolback();
							throw e;
						}

					}

				});
			}
		}

	}
}
