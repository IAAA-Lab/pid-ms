package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Registration;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class NamespaceServiceTest {
    @Autowired
    private NamespaceService namespaceService;

    private Namespace namespace;

    private Registration registration;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    TransactionTemplate transactionTemplate;

    @Before
    public void before() {
        namespace = Fixtures.namespace();
        registration = namespace.getRegistration();
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @After
    public void after() {
        namespaceService.deleteAll();
    }

    @Test
    public void ensureNamespaceHasOptimisticLocking() throws InterruptedException, ExecutionException {
        transactionTemplate.execute(transactionStatus -> {
            registration.setItemStatus(ItemStatus.PENDING_VALIDATION);
            registration.setProcessStatus(ProcessStatus.PENDING_HARVEST);
            namespaceService.createOrUpdateNamespace(namespace);
            return namespace;
        });

        ExecutorService ex = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        Future<Boolean> firstTransaction = ex.submit(new SimpleTransaction(latch));
        Thread.sleep(500);
        Future<Boolean> secondTransaction = ex.submit(new SimpleTransaction(latch));
        latch.await();
        ex.shutdown();
        ex.awaitTermination(2, TimeUnit.SECONDS);
        assertThat("First transaction must be true", firstTransaction.get(), is(true));
        assertThat("Second transaction must be false", secondTransaction.get(), is(false));
    }

    @Test
    @Transactional
    public void prepareForVerification() {
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        namespaceService.createOrUpdateNamespace(namespace);

        Optional<Namespace> ns = namespaceService.prepareForVerification(registration.getNextRenewalDate().plus(1, ChronoUnit.HOURS));
        assertThat("Retrieved namespace must be ES.IGN.NGBE", ns.get().getNamespace(), is("ES.IGN.NGBE"));

        ns = namespaceService.prepareForVerification(registration.getNextRenewalDate().plus(1, ChronoUnit.HOURS));
        assertThat("No more pending process", ns.isPresent(), is(false));
    }


    class SimpleTransaction implements Callable<Boolean> {
        private CountDownLatch latch;

        SimpleTransaction(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public Boolean call() {
                try {
                    transactionTemplate.execute(transactionStatus -> {
                        Namespace ns = namespaceService.findPendingProcess();
                        ns.getRegistration().setProcessStatus(ProcessStatus.HARVEST);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        namespaceService.createOrUpdateNamespace(ns);
                        return null;
                    });
                    return true;
                } catch (ObjectOptimisticLockingFailureException e) {
                    return false;
                } finally {
                    latch.countDown();
                }
        }
    }
}
